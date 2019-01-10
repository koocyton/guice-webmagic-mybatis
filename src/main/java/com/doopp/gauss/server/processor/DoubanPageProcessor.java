package com.doopp.gauss.server.processor;

import com.doopp.gauss.server.dao.FilmDao;
import com.doopp.gauss.server.entity.FilmComment;
import com.doopp.gauss.server.entity.FilmInfo;
import com.doopp.gauss.server.util.IdWorker;
import com.google.common.base.Joiner;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.JsonPathSelector;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DoubanPageProcessor implements PageProcessor {

    private static final Logger logger = LoggerFactory.getLogger(DoubanPageProcessor.class);

    @Inject
    private IdWorker idWorker;

    @Inject
    private FilmDao filmDao;

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10 * 1000)
            .addHeader("Cookie", "ll=\"108288\"; bid=4CbWPar0NmA; _vwo_uuid_v2=D19B718C363D5B6EC53543AE9A83B49D4|dc87475084465bc345cf8ff26e939fe7; douban-fav-remind=1; gr_user_id=d09fdb29-a865-4307-85b8-be94f4261050; viewed=\"26958126_11743091\"; __yadk_uid=aitN1aOLliRysbmqrUgeYAN5P0w3pVZY; ct=y; __utmc=30149280; __utmc=223695111; ap_v=0,6.0; _pk_ref.100001.4cf6=%5B%22%22%2C%22%22%2C1543809776%2C%22https%3A%2F%2Fwww.douban.com%2F%22%5D; _pk_ses.100001.4cf6=*; __utma=30149280.591845928.1521823202.1543802840.1543809776.19; __utmb=30149280.0.10.1543809776; __utmz=30149280.1543809776.19.16.utmcsr=douban.com|utmccn=(referral)|utmcmd=referral|utmcct=/; __utma=223695111.740549006.1542448722.1543802842.1543809776.4; __utmb=223695111.0.10.1543809776; __utmz=223695111.1543809776.4.4.utmcsr=douban.com|utmccn=(referral)|utmcmd=referral|utmcct=/; _pk_id.100001.4cf6=3c01c1554530b7fd.1542448723.4.1543809779.1543805355.")
            .setCharset("UTF-8")
            .addHeader("Referer","https://movie.douban.com/tag/")
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36");

    // TOP250
    private static final String FILM_TOP_LIST = "https://movie.douban.com/top250";

    private static final String FILM_TOP_LIST_REG = "/top250\\?start=\\d+";

    // 列表
    private static final String FILM_LIST = "https://movie.douban.com/j/new_search_subjects?sort=R&tags=%E7%94%B5%E5%BD%B1";

    private static final String FILM_LIST_REG = "/j/new_search_subjects\\?sort=R\\&tags=\\%E7\\%94\\%B5\\%E5\\%BD\\%B1";

    // 电影 URL
    private static final String FILM_URL = "https://movie.douban.com/subject/";

    private static final String FILM_URL_REG = "/subject/(\\d+)/";


    private String getMatcher(String regex, String source) {
        String result = "";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            result = matcher.group(1);
        }
        return result;
     }

    @Override
    public void process(Page page) {

        // TOP 250 分页详细的页面
        if (page.getUrl().regex(FILM_TOP_LIST_REG).match()) {
            // 获取分页 offset
            int pageStart = Integer.valueOf(this.getMatcher("start=(\\d+)", page.getUrl().get()));
            // 下一页
            if (pageStart<225) {
                page.addTargetRequests(new ArrayList<String>(){{
                    add(FILM_TOP_LIST + "?start=" + (25 + pageStart));
                }});
            }
            // 拿到连接
            List<String> topFilmUrl = page.getHtml().xpath("//li//div[@class='item']//div[@class='info']//div[@class='hd']/a/@href").all();
            page.addTargetRequests(topFilmUrl);
        }

        // 分页详细的页面
        else if (page.getUrl().regex(FILM_LIST_REG).match()) {

            // 获取分页 offset
            String pageStart = this.getMatcher("start=(\\d+)", page.getUrl().get());
            String filmGenre = this.getMatcher("genres=(.+)$", page.getUrl().get());

            // 取出电影的数据
            List<String> filmData = new JsonPathSelector("$.data").selectList(page.getRawText());

            // 下一页
            if (filmData.size()>=20) {
                page.addTargetRequests(new ArrayList<String>(){{
                    add(FILM_LIST + "&start=" + (20 + Integer.valueOf(pageStart)) + "&genres=" + filmGenre);
                }});
            }

            // 电影
            if (filmData.size()>=1) {
                List<String> filmUrls = new ArrayList<>();
                for (String filmDatum : filmData) {
                    // 取得 ID
                    String filmId = new JsonPathSelector("$.id").select(filmDatum);
                    filmUrls.add(FILM_URL + filmId + "/");
                }
                page.addTargetRequests(filmUrls);
            }
        }

        // 分页详细的页面
        else if (page.getUrl().regex(FILM_URL_REG).match()) {

            FilmInfo filmInfo = new FilmInfo();

            // id
            filmInfo.setId(idWorker.nextId());

            // 豆瓣 ID
            filmInfo.setDouban_id(Integer.valueOf(page.getUrl().regex(FILM_URL_REG).toString()));

            // 电影名
            filmInfo.setFilm_name(page.getHtml().regex("<title>\\s+(.+)\\s+\\(豆瓣\\)\\s+</title>").toString());
            if (filmInfo.getFilm_name() == null) {
                page.setSkip(true);
                return;
            }

            // 封面
            filmInfo.setFilm_cover(page.getHtml().xpath("//div[@id='mainpic']/a/img/@src").toString());

            // 导演
            List<String> directorList = page.getHtml().xpath("//div[@id='info']//span//span//a[@rel='v:directedBy']/text()").all();
            if (directorList!=null && directorList.size()>=1) {
                filmInfo.setDirectors(Joiner.on(" / ").join(directorList));
            }
            else {
                filmInfo.setDirectors("");
            }

            // 编剧
            String screenwriterHtml = page.getHtml().regex("<span[^>]+>编剧</span>: <span[^>]+>(.+?)</span></span>").toString();
            if (screenwriterHtml!=null) {
                List<String> screenwriterList = new Html(screenwriterHtml).xpath("//a/text()").all();
                filmInfo.setScreenwriters(Joiner.on(" / ").join(screenwriterList));
            }
            else {
                filmInfo.setScreenwriters("");
            }

            // 主演
            List<String> actorList = page.getHtml().xpath("//div[@id='info']//span//span//a[@rel='v:starring']/text()").all();
            if (actorList!=null && actorList.size()>=1) {
                filmInfo.setActors(Joiner.on(" / ").join(actorList));
            }
            else {
                filmInfo.setActors("");
            }

            // 类型
            List<String> genreList = page.getHtml().xpath("//div[@id='info']//span[@property='v:genre']/text()").all();
            if (genreList!=null && genreList.size()>=1) {
                filmInfo.setGenres(Joiner.on(" / ").join(genreList));
            }
            else {
                filmInfo.setGenres("");
            }

            // 制片国家/地区
            filmInfo.setRegion(page.getHtml().regex("<span[^>]+>制片国家/地区:</span>\\s+(.+?)\\s+<br").toString());
            if (filmInfo.getRegion()==null) {
                filmInfo.setRegion("");
            }

            // 语言
            filmInfo.setLanguage(page.getHtml().regex("<span[^>]+>语言:</span>\\s+(.+?)\\s+<br").toString());
            if (filmInfo.getLanguage()==null) {
                filmInfo.setLanguage("");
            }

            // 上映日期
            List<String> releaseDateList = page.getHtml().xpath("//div[@id='info']//span[@property='v:initialReleaseDate']/text()").all();
            if (releaseDateList!=null && releaseDateList.size()>=1) {
                filmInfo.setRelease_date(Joiner.on(" / ").join(releaseDateList));
            }
            else {
                filmInfo.setRelease_date("");
            }

            // 片长
            String film_duration = page.getHtml().xpath("//div[@id='info']//span[@property='v:runtime']/text()").regex("(\\d+)").toString();
            if (film_duration==null) {
                filmInfo.setFilm_duration(0);
            }
            else {
                filmInfo.setFilm_duration(Integer.valueOf(film_duration));
            }

            // 又名
            filmInfo.setAlias(page.getHtml().regex("<span[^>]+>又名:</span>\\s+(.+?)\\s+<br").toString());
            if (filmInfo.getAlias()==null) {
                filmInfo.setAlias("");
            }

            // MDb链接: tt7362036
            filmInfo.setImdb_link(page.getHtml().xpath("//div[@id='info']//a[@href~='http://www.imdb.com']/@href").toString());
            if (filmInfo.getImdb_link()==null) {
                filmInfo.setImdb_link("");
            }

            // 评分
            String douban_rating = page.getHtml().xpath("//strong[@class='ll rating_num']/text()").toString();
            if (douban_rating==null) {
                filmInfo.setDouban_rating(0);
            }
            else {
                filmInfo.setDouban_rating(Float.valueOf(douban_rating));
            }

            // 评分
            filmInfo.setImdb_rating(0);

            // 保存电影信息
            filmDao.saveInfo(filmInfo);

            // 评论
            List<String> cVoteList = page.getHtml().xpath("//div[@class='comment']//h3//span[@class='comment-vote']//span/text()").all();
            List<String> cUserList = page.getHtml().xpath("//div[@class='comment']//h3//span[@class='comment-info']//a/text()").all();
            List<String> cDateList = page.getHtml().xpath("//div[@class='comment']//h3//span[@class='comment-info']//span[@class='comment-time']/@title").all();
            List<String> cContentList = page.getHtml().xpath("//div[@class='comment']//p//span/text()").all();

            String[] cVotes = cVoteList.toArray(new String[cVoteList.size()]);
            String[] cUsers = cUserList.toArray(new String[cUserList.size()]);
            String[] cDates = cDateList.toArray(new String[cDateList.size()]);
            String[] cContents = cContentList.toArray(new String[cContentList.size()]);

            for(int ii=0; ii<cVotes.length; ii++) {
                FilmComment filmComment = new FilmComment();
                filmComment.setVote(Integer.valueOf(cVotes[ii]));
                filmComment.setContent(cContents[ii]);
                filmComment.setUser_name(cUsers[ii]);
                filmComment.setDate_time(cDates[ii]);
                filmComment.setFilm_name(filmInfo.getFilm_name());
                filmComment.setFilm_id(filmInfo.getId());
                filmComment.setComment_id(idWorker.nextId());
                // 保存评论
                filmDao.saveComment(filmComment);
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}