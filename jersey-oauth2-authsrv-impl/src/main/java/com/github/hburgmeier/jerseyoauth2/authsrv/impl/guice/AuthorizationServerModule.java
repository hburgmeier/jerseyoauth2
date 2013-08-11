package com.github.hburgmeier.jerseyoauth2.authsrv.impl.guice;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IRequestFactory;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IAuthorizationService;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.ITokenService;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.services.AuthorizationService;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.services.IntegratedAccessTokenVerifier;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.services.TokenService;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.RequestFactory;
import com.github.hburgmeier.jerseyoauth2.rs.api.token.IAccessTokenVerifier;
import com.google.inject.AbstractModule;

public class AuthorizationServerModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(IAuthorizationService.class).to(AuthorizationService.class);
		bind(ITokenService.class).to(TokenService.class);
		bind(IAccessTokenVerifier.class).to(IntegratedAccessTokenVerifier.class);
		bind(IRequestFactory.class).to(RequestFactory.class);
	}

}
