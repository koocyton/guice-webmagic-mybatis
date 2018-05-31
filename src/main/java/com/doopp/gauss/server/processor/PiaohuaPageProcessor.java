//package com.doopp.gauss.server.processor;
//
//import com.doopp.gauss.server.dao.MovieDao;
//import com.doopp.gauss.server.entity.Movie;
//import com.doopp.gauss.server.util.IdWorker;
//import com.google.gson.Gson;
//import com.google.inject.Inject;
//import com.google.inject.Injector;
//import us.codecraft.webmagic.Page;
//import us.codecraft.webmagic.Site;
//import us.codecraft.webmagic.Spider;
//import us.codecraft.webmagic.processor.PageProcessor;
//
//import java.util.List;
//
//public class PiaohuaPageProcessor implements PageProcessor {
//
//    @Inject
//    private Injector injector;
//
//    @Inject
//    private MovieDao movieDao;
//
//    @Inject
//    private IdWorker idWorker;
//
//    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);
//
//    @Override
//    public void process(Page page) {
//        // https://www.piaohua.com/html/kehuan/2018/0420/33577.html
//        // page.addTargetRequests(page.getHtml().links().regex("(https://www\\.piaohua\\.com/\\w+/\\w+/\\d+/\\d+/\\d+\\.html)").all());
//        page.addTargetRequests(page.getHtml().links().regex("(https://www\\.piaohua\\.com/html/\\w+/.+)").all());
//        // page.addTargetRequests(page.getHtml().links().all());
//
//        // https://www.piaohua.com/html/kehuan/2018/0518/33697.html
//        page.putField("type", page.getUrl().regex("https://www\\.piaohua\\.com/html/(\\w+)/.+").toString());
//        // page.putField("author", page.getUrl().regex("https://www\\.piaohua\\.com/(\\w+)/.*").toString());
//
//        // <title>陆地空谷下载_迅雷下载_免费下载_飘花电影网</title>
//        page.putField("name", page.getHtml().regex("<title>(.+)下载_迅雷.+</title>").toString());
//        // page.putField("name", page.getHtml().xpath("//h1[@class='public']/strong/a/text()").toString());
//        if (page.getResultItems().get("name")==null){
//            page.setSkip(true);
//        }
//        // page.putField("readme", page.getHtml().xpath("//div[@id='readme']/tidyText()"));
//
//        // 下载地址和剧情：<br />
//        // <img border="0" src="http://img04.taobaocdn.com/imgextra/i4/229823360/T2VAjUXddaXXXXXXXX_!!229823360.jpg" /><br />
//        page.putField("cover", page.getHtml().regex("<div id=\"showinfo\" .+?<img [^>]+ src=\"([^\"]+)\"").toString());
//        // page.putField("cover", page.getHtml().xpath("//div[@id='showinfo']/img/@src"));
//        page.putField("resource", page.getHtml().regex("<a href=\"([magnet|ftp][^\"]+)\"").all());
//
//        String from_url = page.getUrl().get();
//        String name = page.getResultItems().get("name");
//        String type = page.getResultItems().get("type");
//        String cover = page.getResultItems().get("cover");
//        List<String> resource = page.getResultItems().get("resource");
//
//        if (name!=null && cover!=null && resource!=null) {
//            Movie movie = new Movie() {{
//                setId(idWorker.nextId());
//                setName(name);
//                setCover(cover);
//                setType(type);
//                setFrom_url(from_url);
//                setIntro("");
//                setResources((new Gson()).toJson(resource));
//            }};
//            movieDao.create(movie);
//        }
//    }
//
//    @Override
//    public Site getSite() {
//        return site;
//    }
//
//    public void run() {
//        Spider.create(injector.getInstance(PiaohuaPageProcessor.class)).addUrl("https://www.piaohua.com/").thread(1000).run();
//    }
//}
