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
        page.addTargetRequests(page.getHtml().links().regex("https://www\\.piaohua.\\.com/.*").all());
        page.putField("type", page.getUrl().regex("https://www\\.piaohua.\\.com/html/(\\w+)").toString());
        /*
        page.putField("name", page.getHtml().xpath("//h1[@class='entry-title public']/strong/a/text()").toString());
        if (page.getResultItems().get("name")==null){
            //skip this page
            page.setSkip(true);
        }
        page.putField("readme", page.getHtml().xpath("//div[@id='readme']/tidyText()"));
        */
    }

    @Override
    public Site getSite() {
        return site;
    }

    public void run() {
        Spider.create(new PiaohuaPageProcessor()).addUrl("https://www.piaohua.com/").thread(5).run();
    }
}
