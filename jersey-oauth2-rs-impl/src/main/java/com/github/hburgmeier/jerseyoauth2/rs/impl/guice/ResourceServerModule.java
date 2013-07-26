package com.github.hburgmeier.jerseyoauth2.rs.impl.guice;

import com.github.hburgmeier.jerseyoauth2.rs.impl.base.AccessTokenVerifierProvider;
import com.github.hburgmeier.jerseyoauth2.rs.impl.base.ConfigurationServiceProvider;
import com.google.inject.AbstractModule;

public class ResourceServerModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(AccessTokenVerifierProvider.class);
		bind(ConfigurationServiceProvider.class);
	}

}
