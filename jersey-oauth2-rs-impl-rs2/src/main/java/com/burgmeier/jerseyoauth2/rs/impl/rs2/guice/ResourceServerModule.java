package com.burgmeier.jerseyoauth2.rs.impl.rs2.guice;

import com.burgmeier.jerseyoauth2.rs.impl.base.AccessTokenVerifierProvider;
import com.burgmeier.jerseyoauth2.rs.impl.base.ConfigurationServiceProvider;
import com.google.inject.AbstractModule;

public class ResourceServerModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(AccessTokenVerifierProvider.class);
		bind(ConfigurationServiceProvider.class);
	}

}
