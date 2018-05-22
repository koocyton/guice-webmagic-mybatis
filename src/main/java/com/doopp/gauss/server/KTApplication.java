package com.doopp.gauss.server;

import com.doopp.gauss.server.dao.MovieDao;
import com.doopp.gauss.server.module.ApplicationModule;
import com.doopp.gauss.server.application.ApplicationProperties;
import com.doopp.gauss.server.database.HikariDataSourceProvider;
import com.doopp.gauss.server.processor.PiaohuaPageProcessor;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.datasource.helper.JdbcHelper;

public class KTApplication {

    public static void main(String[] args) throws Exception {
        System.setProperty("applicationPropertiesConfig", args[0]);
        Injector injector = Guice.createInjector(
                new myBatisModule(),
                new ApplicationModule()
        );
        final PiaohuaPageProcessor processor = injector.getInstance(PiaohuaPageProcessor.class);
        processor.run();
    }

    private static class myBatisModule extends MyBatisModule {
        @Override
        protected void initialize() {
            install(JdbcHelper.MySQL);
            bindDataSourceProviderType(HikariDataSourceProvider.class);
            bindTransactionFactoryType(JdbcTransactionFactory.class);
            addMapperClass(MovieDao.class);
            Names.bindProperties(binder(), new ApplicationProperties());
        }
    }
}
