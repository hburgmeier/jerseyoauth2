package com.burgmeier.jerseyoauth2.rs.impl.guice;

import com.burgmeier.jerseyoauth2.rs.impl.filter.AccessTokenVerifierProvider;
import com.burgmeier.jerseyoauth2.rs.impl.filter.ConfigurationServiceProvider;
import com.google.inject.AbstractModule;

public class ResourceServerModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(AccessTokenVerifierProvider.class);
		bind(ConfigurationServiceProvider.class);
	}

}
