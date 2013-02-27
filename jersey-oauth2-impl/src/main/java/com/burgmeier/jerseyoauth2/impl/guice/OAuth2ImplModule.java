package com.burgmeier.jerseyoauth2.impl.guice;

import com.burgmeier.jerseyoauth2.impl.filter.AccessTokenServiceProvider;
import com.google.inject.AbstractModule;

public class OAuth2ImplModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(AccessTokenServiceProvider.class);		
	}

}
