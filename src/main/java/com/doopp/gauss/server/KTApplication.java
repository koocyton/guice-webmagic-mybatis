package com.doopp.gauss.server;

import com.doopp.gauss.server.dao.FilmDao;
import com.doopp.gauss.server.database.HikariDataSourceProvider;
import com.doopp.gauss.server.module.ApplicationModule;
import com.doopp.gauss.server.application.ApplicationProperties;
import com.doopp.gauss.server.processor.DoubanPageProcessor;
import com.doopp.gauss.server.processor.PiaohuaPageProcessor;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.datasource.helper.JdbcHelper;
import us.codecraft.webmagic.Spider;

import java.net.URLEncoder;

public class KTApplication {

    public static void main(String[] args) throws Exception {
        System.setProperty("applicationPropertiesConfig", args[0]);

        Injector injector = Guice.createInjector(
                new myBatisModule(),
                new ApplicationModule()
        );


        // 豆瓣
        if (args[1].contains("douban")) {
            Spider.create(injector.getInstance(DoubanPageProcessor.class))
                    .addUrl("https://movie.douban.com/j/new_search_subjects?sort=R&tags=%E7%94%B5%E5%BD%B1&start=0&genres=" + URLEncoder.encode("动作", "utf-8"))
                    .addUrl("https://movie.douban.com/j/new_search_subjects?sort=R&tags=%E7%94%B5%E5%BD%B1&start=0&genres=" + URLEncoder.encode("喜剧", "utf-8"))
                    .addUrl("https://movie.douban.com/j/new_search_subjects?sort=R&tags=%E7%94%B5%E5%BD%B1&start=0&genres=" + URLEncoder.encode("爱情", "utf-8"))
                    .addUrl("https://movie.douban.com/j/new_search_subjects?sort=R&tags=%E7%94%B5%E5%BD%B1&start=0&genres=" + URLEncoder.encode("科幻", "utf-8"))
                    .addUrl("https://movie.douban.com/j/new_search_subjects?sort=R&tags=%E7%94%B5%E5%BD%B1&start=0&genres=" + URLEncoder.encode("剧情", "utf-8"))
                    .addUrl("https://movie.douban.com/j/new_search_subjects?sort=R&tags=%E7%94%B5%E5%BD%B1&start=0&genres=" + URLEncoder.encode("悬疑", "utf-8"))
                    .addUrl("https://movie.douban.com/j/new_search_subjects?sort=R&tags=%E7%94%B5%E5%BD%B1&start=0&genres=" + URLEncoder.encode("战争", "utf-8"))
                    .addUrl("https://movie.douban.com/j/new_search_subjects?sort=R&tags=%E7%94%B5%E5%BD%B1&start=0&genres=" + URLEncoder.encode("恐怖", "utf-8"))
                    .addUrl("https://movie.douban.com/j/new_search_subjects?sort=R&tags=%E7%94%B5%E5%BD%B1&start=0&genres=" + URLEncoder.encode("灾难", "utf-8"))
                    .addUrl("https://movie.douban.com/j/new_search_subjects?sort=R&tags=%E7%94%B5%E5%BD%B1&start=0&genres=" + URLEncoder.encode("动漫", "utf-8"))
                    .addUrl("https://movie.douban.com/top250?start=0")
                    .thread(1)
                    .run();
        }

        // 飘花
        if (args[1].contains("piaohua")) {
            Spider.create(injector.getInstance(PiaohuaPageProcessor.class))
                    .addUrl("https://www.piaohua.com/html/dongzuo/list_1.html")
                    .addUrl("https://www.piaohua.com/html/xiju/list_1.html")
                    .addUrl("https://www.piaohua.com/html/aiqing/list_1.html")
                    .addUrl("https://www.piaohua.com/html/kehuan/list_1.html")
                    .addUrl("https://www.piaohua.com/html/juqing/list_1.html")
                    .addUrl("https://www.piaohua.com/html/xuanyi/list_1.html")
                    .addUrl("https://www.piaohua.com/html/zhanzheng/list_1.html")
                    .addUrl("https://www.piaohua.com/html/kongbu/list_1.html")
                    .addUrl("https://www.piaohua.com/html/zainan/list_1.html")
                    .addUrl("https://www.piaohua.com/html/dongman/list_1.html")
                    .thread(400)
                    .run();
        }
    }

    private static class myBatisModule extends MyBatisModule {
        @Override
        protected void initialize() {
            install(JdbcHelper.MySQL);
            bindDataSourceProviderType(HikariDataSourceProvider.class);
            bindTransactionFactoryType(JdbcTransactionFactory.class);
            addMapperClass(FilmDao.class);
            Names.bindProperties(binder(), new ApplicationProperties());
        }
    }
}
