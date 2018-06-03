package com.doopp.gauss.server.processor;

import com.doopp.gauss.server.dao.MovieDao;
import com.doopp.gauss.server.entity.Movie;
import com.doopp.gauss.server.util.IdWorker;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Injector;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

public class DyttPageProcessor implements PageProcessor {

    @Inject
    private Injector injector;

    @Inject
    private MovieDao movieDao;

    @Inject
    private IdWorker idWorker;

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    @Override
    public void process(Page page) {
        // http://dytt8.net/html/gndy/dyzz/20180529/56914.html
        // page.addTargetRequests(page.getHtml().links().regex("(https://www\\.piaohua\\.com/\\w+/\\w+/\\d+/\\d+/\\d+\\.html)").all());
        page.addTargetRequests(page.getHtml().links().regex("(/html/\\w+/\\w+/\\d+/\\d+\\.html)").all());

        // <title>2018年喜剧《萌犬好声音3》BD国英双语双字幕迅雷下载_电影天堂</title>
        page.putField("name", page.getHtml().regex("<title>[^《]+《([^》]+)》[^<]+</title>").toString());
        if (page.getResultItems().get("name")==null){
            page.setSkip(true);
        }

        // cover
        page.putField("cover", page.getHtml().regex("<!--Content Start-->.+?<img[^>]+src=\"([^\"]+)\"").toString());

        // get film type
        page.putField("publish_date", page.getUrl().regex("/html/\\w+/\\w+/(\\d+)/\\d+\\.html").toString());

        // xpath example
        // page.putField("name", page.getHtml().xpath("//h1[@class='public']/strong/a/text()").toString());

        page.putField("test_category", page.getHtml().regex("◎类　　别　([^<]+)").toString());
        if (page.getResultItems().get("test_category")==null){
            match2(page);
        }
        else {
            match1(page);
        }

        String name = page.getResultItems().get("name");
        String type = "video";
        String intro = page.getResultItems().get("intro");
        String first_category = "";
        String second_category = "";
        String third_category = "";
        String cover = page.getResultItems().get("cover");
        String publish_date = page.getResultItems().get("publish_date");
        String translate_name = page.getResultItems().get("translate_name");
        String film_name = page.getResultItems().get("film_name");
        String release_age = page.getResultItems().get("years");
        String origin_place = page.getResultItems().get("origin_place");
        String category = page.getResultItems().get("category");
        String language = page.getResultItems().get("language");
        String subtitle = page.getResultItems().get("subtitle");
        String release_date = page.getResultItems().get("release_date");
        String video_format = page.getResultItems().get("video_format");
        String video_size = page.getResultItems().get("video_size");
        String file_size = page.getResultItems().get("file_size");
        String time_length = page.getResultItems().get("time_length");
        String director = page.getResultItems().get("director");
        List<String> actors = page.getResultItems().get("actors");
        List<String> download_links = page.getResultItems().get("download_links");
        String from_url = page.getUrl().get();

        if (name!=null && cover!=null && download_links!=null) {
            Movie movie = new Movie() {{
                setId(idWorker.nextId());
                setName(name);
                setCover(cover);
                setType(type);
                setFrom_url(from_url);
                setIntro(intro==null ? "" : subtitle);
                setFirst_category(first_category);
                setSecond_category(second_category);
                setThird_category(third_category);
                setPublish_date(publish_date==null ? "" : publish_date);
                setTranslate_name(translate_name==null ? "" : translate_name);
                setFilm_name(film_name==null ? "" : film_name);
                setRelease_age(release_age==null ? "" : release_age);
                setOrigin_place(origin_place==null ? "" : origin_place);
                setCategory(category==null ? "" : category);
                setLanguage(language==null ? "" : language);
                setSubtitle(subtitle==null ? "" : subtitle);
                setRelease_date(release_date==null ? "1970-01-01" : release_date);
                setVideo_format(video_format==null ? "" : video_format);
                setVideo_size(video_size==null ? "" : video_size);
                setFile_size(file_size==null ? "" : file_size);
                setTime_length(time_length==null ? "" : time_length);
                setDirector(director==null ? "" : director);
                setActors((new Gson()).toJson(actors));
                setDownload_links((new Gson()).toJson(download_links));
            }};
            movieDao.create(movie);
        }
    }

    private void match1(Page page) {

        // <br />◎译　　名　The First Stab of The Datang <br />◎片　　名　大唐第一刺局 <br />◎年　　代　2018 <br />◎产　　地　中国
        // <br />◎类　　别　动作/悬疑/武侠 <br />◎语　　言　普通话 <br />◎字　　幕　中文
        // <br />◎上映日期　2018-05-30(中国) <br />◎文件格式　x264 + aac <br />◎视频尺寸　1920 x 816
        // <br />◎文件大小　1CD <br />◎片　　长　65分钟 <br />◎导　　演　赵聪 <br />
        page.putField("translate_name", page.getHtml().regex("◎译　　名　([^<]+)").toString());
        page.putField("film_name", page.getHtml().regex("◎片　　名　([^<]+)").toString());
        page.putField("years", page.getHtml().regex("◎年　　代　([^<]+)").toString());
        page.putField("origin_place", page.getHtml().regex("◎产　　地　([^<]+)").toString());
        page.putField("category", page.getHtml().regex("◎类　　别　([^<]+)").toString());
        page.putField("language", page.getHtml().regex("◎语　　言　([^<]+)").toString());
        page.putField("subtitle", page.getHtml().regex("◎字　　幕　([^<]+)").toString());
        page.putField("release_date", page.getHtml().regex("◎上映日期　(\\d{4}-\\d{2}-\\d{2})").toString());
        page.putField("video_format", page.getHtml().regex("◎文件格式　([^<]+)").toString());
        page.putField("video_size", page.getHtml().regex("◎视频尺寸　([^<]+)").toString());
        page.putField("file_size", page.getHtml().regex("◎文件大小　([^<]+)").toString());
        page.putField("time_length", page.getHtml().regex("◎片　　长　([^<]+)").toString());
        page.putField("director", page.getHtml().regex("◎导　　演　([^<]+)").toString());
        // ◎主　　演　李思捷 Sze-Chit Lee <br />　　　　　　狄龙 Lung Ti <br />　　　　　　元秋 Qiu Yuen <br />　　　　　　钱小豪 Siu-hou Chin <br />　　　　　　林敏骢 Man-Chung Lam <br />　　　　　　苏玉华 Yuk-Wah So <br />　　　　　　罗家英 Kar-Ying Law <br />　　　　　　廖伟雄 Wai Hung Liu <br />　　　　　　钱莹 fion <br />　　　　　　李俊毅 Junyi Li <br />
        page.putField("actor_list", page.getHtml().regex("◎主　　演　(.+)<br><br>◎简　　介").toString());
        if (page.getResultItems().get("actor_list")!=null) {
            Html actor_html = new Html("<br>　　　　　　" + page.getResultItems().get("actor_list"));
            page.putField("actors", actor_html.regex("<br>　+([^\\n]+) ").all());
        }

        // 简介
        page.putField("intro", page.getHtml().regex("◎简　　介 <br><br>　+(.+?)<br><br>").toString());
        // 截图
        page.putField("screenshot", page.getHtml().regex("◎简　　介.+<img[^>]+src=\"([^\"]+)\".+下载地址").toString());
        // 下载地址
        page.putField("download_links", page.getHtml().regex("<a href=\"([magnet|ftp][^\"]+)\"").all());
    }

    private void match2(Page page) {

        page.putField("translate_name", page.getHtml().regex("【译    　名】： ([^<]+)").toString());
        page.putField("film_name", page.getHtml().regex("【原    　名】： ([^<]+)").toString());
        page.putField("years", page.getHtml().regex("【年    　代】： (\\d{4})").toString());
        page.putField("origin_place", page.getHtml().regex("【国    　家】：([^<]+)").toString());
        page.putField("category", page.getHtml().regex("【类    　别】： ([^<]+)").toString());
        page.putField("language", page.getHtml().regex("【语    　言】： ([^<]+)").toString());
        page.putField("subtitle", page.getHtml().regex("【字    　幕】：([^<]+)").toString());
        page.putField("release_date", page.getHtml().regex("【首    　播】：(\\d{4}-\\d{2}-\\d{2})").toString());
        page.putField("video_format", page.getHtml().regex("◎文件格式　([^<]+)").toString());
        page.putField("video_size", page.getHtml().regex("◎视频尺寸　([^<]+)").toString());
        page.putField("file_size", page.getHtml().regex("◎文件大小　([^<]+)").toString());
        page.putField("time_length", page.getHtml().regex("【片    　长】： ([^<]+)").toString());
        page.putField("director", page.getHtml().regex("【导    　演】：([^【]+)【").toString());
        // ◎主　　演　李思捷 Sze-Chit Lee <br />　　　　　　狄龙 Lung Ti <br />　　　　　　元秋 Qiu Yuen <br />　　　　　　钱小豪 Siu-hou Chin <br />　　　　　　林敏骢 Man-Chung Lam <br />　　　　　　苏玉华 Yuk-Wah So <br />　　　　　　罗家英 Kar-Ying Law <br />　　　　　　廖伟雄 Wai Hung Liu <br />　　　　　　钱莹 fion <br />　　　　　　李俊毅 Junyi Li <br />
        page.putField("actor_list", page.getHtml().regex("【演    　员】：(.+)<br><br>◎简　　介").toString());
        if (page.getResultItems().get("actor_list")!=null) {
            Html actor_html = new Html("<br>　　　　　　" + page.getResultItems().get("actor_list"));
            page.putField("actors", actor_html.regex("<br>　+([^\\n]+) ").all());
        }

        // 简介
        page.putField("intro", page.getHtml().regex("【简    　介】：<br><br>　+(.+?)<br><br>").toString());
        // 截图
        page.putField("screenshot", page.getHtml().regex("【简    　介】：.+<img[^>]+src=\"([^\"]+)\".+下载地址").toString());
        // 下载地址
        page.putField("download_links", page.getHtml().regex("<a href=\"([magnet|ftp][^\"]+)\"").all());
    }

    @Override
    public Site getSite() {
        return site;
    }

    public void run() {
        Spider.create(injector.getInstance(DyttPageProcessor.class)).addUrl("http://dytt8.net/").thread(10).run();
    }
}

/*

【译    　名】： 邪恶力量/超自然档案/凶鬼恶灵
【原    　名】： Supernatural
【年    　代】： 2005-201x
【国    　家】： 美国
【类    　别】： 动作|冒险|剧情片|恐怖/灵异|科幻
【语    　言】： 英语
【首    　播】： 2010年9月24日（周五晚9点）
【电  视  台】： The CW ( USA)
【IMDB评分】： 8.8/10 from 181,380 users
【集    　数】： 预计 23 集
【片    　长】： 平均 60 分钟
【导    　演】：• Philip Sgriccia  ...  (21集, 2006-2011)
　　    　　　　• Robert Singer  ...  (20集, 2005-2011)
　　    　　　　• Kim Manners  ...  (16集, 2005-2008)
　　    　　　　• Charles Beeson  ...  (12集, 2007-2011)
　　    　　　　• Mike Rohl  ...  (8集, 2006-2011)
　　    　　　　• Steve Boyum  ...  (7集, 2006-2010)
　　    　　　　• J. Miller Tobin  ...  (4集, 2007-2009)
　　    　　　　• James L. Conway  ...  (4集, 2009-2010)
【编    　剧】：• Eric Kripke  ...  (126集, 2005-2011)
　　    　　　　• Sera Gamble  ...  (24集, 2005-2011)
　　    　　　　• Ben Edlund  ...  (16集, 2006-2011)
　　    　　　　• Jeremy Carver  ...  (12集, 2007-2010)
　　    　　　　• Andrew Dabb  ...  (11集, 2008-2011)
　　    　　　　• Daniel Loflin  ...  (11集, 2008-2011)
　　    　　　　• John Shiban  ...  (9集, 2005-2007)
　　    　　　　• Raelle Tucker  ...  (8集, 2005-2007)
　　    　　　　• Cathryn Humphris  ...  (7集, 2006-2009)
【演    　员】：• Jared Padalecki  饰演  Sam Winchester (126集, 2005-2011)
　　    　　　　• Jensen Ackles  饰演  Dean Winchester (126集, 2005-2011)
　　    　　　　• Jim Beaver  饰演  Bobby Singer (42集, 2006-2011)
　　    　　　　• Misha Collins  饰演  Castiel (40集, 2008-2011)
　　    　　　　•

【简    　介】：

　　《邪恶力量》（Supernatural），又译《凶鬼恶灵》、《狙魔人》、《超自然档案》、《神秘力量》，是由美国WB电视台（现更名为CW电视台）播出的讲述灵异超自然现象的电视剧。在加拿大拍摄。该剧主要是在讲述两兄弟山姆·温彻斯特和迪恩·温彻斯特开着1967年的黑色雪佛兰“羚羊” 穿梭在美国各处调查超自然或不可思议的事件并与之战斗的故事，大多来自于美国的都市传说和民间传说。

　　山姆和迪安两兄弟将继续他们危机四伏的使命。他们遇到了最强劲的对手：the Devil。最终他们找出了方法迫使Lucifer重回地狱，结束了灾难，但是付出了惨痛的代价：山姆的生命。新一季将充满神秘和阴影，随着第五季的大灾难，天堂和地狱变得杂乱无章。怪物、天使和恶魔无秩序、混乱的到处游荡。迪安也不再从事追魔行动，并发誓再也不回来，回到了原先的生活。他遇到了从地狱逃出生天的山姆，两人重聚后发现他们再也回不到过去，他们的关系也和从前不一样了，一切都变了。

 */