package com.doopp.gauss.server.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class PiaohuaPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    @Override
    public void process(Page page) {
        // https://www.piaohua.com/html/kehuan/2018/0420/33577.html
        // page.addTargetRequests(page.getHtml().links().regex("(https://www\\.piaohua\\.com/\\w+/\\w+/\\d+/\\d+/\\d+\\.html)").all());
        page.addTargetRequests(page.getHtml().links().regex("(https://www\\.piaohua\\.com/html/\\w+/.+)").all());
        // page.addTargetRequests(page.getHtml().links().all());

        // https://www.piaohua.com/html/kehuan/2018/0518/33697.html
        page.putField("type", page.getUrl().regex("https://www\\.piaohua\\.com/html/(\\w+)/.+").toString());
        // page.putField("author", page.getUrl().regex("https://www\\.piaohua\\.com/(\\w+)/.*").toString());

        // <title>陆地空谷下载_迅雷下载_免费下载_飘花电影网</title>
        page.putField("name", page.getHtml().regex("<title>(.+)下载_迅雷.+</title>").toString());
        // page.putField("name", page.getHtml().xpath("//h1[@class='public']/strong/a/text()").toString());
        if (page.getResultItems().get("name")==null){
            page.setSkip(true);
        }
        // page.putField("readme", page.getHtml().xpath("//div[@id='readme']/tidyText()"));

        // 下载地址和剧情：<br />
        // <img border="0" src="http://img04.taobaocdn.com/imgextra/i4/229823360/T2VAjUXddaXXXXXXXX_!!229823360.jpg" /><br />
        page.putField("cover", page.getHtml().regex("<div id=\"showinfo\" .+?<img [^>]+ src=\"([^\"]+)\"").toString());
        // page.putField("cover", page.getHtml().xpath("//div[@id='showinfo']/img/@src"));
        page.putField("resource_html", page.getHtml().regex("(<table bgcolor=\"#ff8c00\" [^>]+>.+?</table>)+").toString());
    }

    @Override
    public Site getSite() {
        return site;
    }

    public void run() {
        Spider.create(new PiaohuaPageProcessor()).addUrl("https://www.piaohua.com/").thread(5).run();
    }
}
