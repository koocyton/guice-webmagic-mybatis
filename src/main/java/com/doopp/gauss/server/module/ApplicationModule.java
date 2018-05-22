package com.doopp.gauss.server.module;

import com.doopp.gauss.server.util.IdWorker;
import com.doopp.gauss.server.application.ApplicationProperties;
import com.google.inject.*;

public class ApplicationModule extends AbstractModule {

	@Singleton
	@Provides
	public IdWorker idWorker() {
		return new IdWorker(1, 1);
	}

	@Singleton
	@Provides
	public ApplicationProperties applicationProperties() {
		return new ApplicationProperties();
	}

}
