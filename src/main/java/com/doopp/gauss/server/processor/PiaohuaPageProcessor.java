package com.doopp.gauss.server.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class PiaohuaPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    @Override
    public void process(Page page) {
        // https://www.piaohua.com/html/juqing/2018/0521/33706.html
        page.addTargetRequests(page.getHtml().links().regex("https://www\\.piaohua\\.com/html/.+").all());
        page.putField("type", page.getUrl().regex("https://www\\.piaohua\\.com/html/(\\w+)/\\d+").toString());
        page.putField("name", page.getHtml().regex("<title>(.+)下载_迅雷下载_免费下载_飘花电影网</title>").toString());
        if (page.getResultItems().get("name")==null) {
            page.setSkip(true);
        }
        page.putField("cover", page.getHtml().regex("<div id=\"showinfo\" [^>]+>.+?<img [^>]+ src=\"([^\"]+)\"").toString());
        // page.putField("resource", page.getHtml().regex("<table bgcolor=\"#ff8c00\" .+?<a href=\"([^\"]+)\"").toString());
        page.putField("resource_html", page.getHtml().regex("<div class=\"play-list-box down-list-box\">.+?(<table bgcolor=\"#ff8c00\" [^>]+>.+</table>).+?</div>").toString());
    }

    @Override
    public Site getSite() {
        return site;
    }

    public void run() {
        Spider.create(new PiaohuaPageProcessor()).addUrl("https://www.piaohua.com/").thread(5).run();
    }
}
