package com.burgmeier.jerseyoauth2.impl.guice;

import com.burgmeier.jerseyoauth2.api.client.IAuthorizationService;
import com.burgmeier.jerseyoauth2.api.token.ITokenService;
import com.burgmeier.jerseyoauth2.impl.filter.AccessTokenServiceProvider;
import com.burgmeier.jerseyoauth2.impl.filter.ConfigurationServiceProvider;
import com.burgmeier.jerseyoauth2.impl.services.AuthorizationService;
import com.burgmeier.jerseyoauth2.impl.services.TokenService;
import com.google.inject.AbstractModule;

public class OAuth2ImplModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(AccessTokenServiceProvider.class);
		bind(ConfigurationServiceProvider.class);
		
		bind(IAuthorizationService.class).to(AuthorizationService.class);
		bind(ITokenService.class).to(TokenService.class);
	}

}
