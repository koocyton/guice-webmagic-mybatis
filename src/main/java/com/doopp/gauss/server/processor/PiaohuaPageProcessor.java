package com.doopp.gauss.server.processor;

import com.doopp.gauss.server.dao.FilmDao;
import com.doopp.gauss.server.entity.FilmSource;
import com.doopp.gauss.server.util.IdWorker;
import com.google.common.base.Joiner;
import com.google.inject.Inject;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

public class PiaohuaPageProcessor implements PageProcessor {

    private static final Logger logger = LoggerFactory.getLogger(PiaohuaPageProcessor.class);

    @Inject
    private FilmDao filmDao;

    @Inject
    private IdWorker idWorker;

    private Site site = Site.me().setRetryTimes(3).setSleepTime(10);

    // 列表
    private static final String FILM_LIST = "/html/\\w+/list_\\d+\\.html";

    // 电影 URL
    private static final String FILM_URL = "/html/\\w+/\\d+/\\d+/\\d+\\.html";

    @Override
    public void process(Page page) {

        // 如果是列表
        if (page.getUrl().regex(FILM_LIST).match()) {
            // 将分页加入目标
            page.addTargetRequests(page.getHtml().links().regex(FILM_LIST).all());
            // 将详细加入目标
            page.addTargetRequests(page.getHtml().links().regex(FILM_URL).all());
        }

        // 分页详细的页面
        else if (page.getUrl().regex(FILM_URL).match()) {

            FilmSource filmSource = new FilmSource();

            // id
            filmSource.setSource_id(idWorker.nextId());

            // default film_id
            filmSource.setFilm_id(0L);

            // 网站
            filmSource.setSource_site("piaohua");

            // url
            filmSource.setSource_url(page.getUrl().get());

            // 电影名
            filmSource.setFilm_name(page.getHtml().regex("<title>(.+?)(CD|DVD|TS|HD|BD|下载).+</title>").toString());
            if (filmSource.getFilm_name() == null) {
                page.setSkip(true);
                return;
            }

            // 下载地址
            List<String> filmDownloads = page.getHtml().xpath("//a[@href~=(ftp|magnet|btbo|ed2k|thunder)]/@href").all();
            if (filmDownloads.size() == 0) {
                logger.info("{} -> {}", filmSource.getSource_url(), filmDownloads);
                page.setSkip(true);
                return;
            }
            filmSource.setSource_data(Joiner.on(" / ").join(filmDownloads));

            // 保存
            filmDao.saveSource(filmSource);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}
