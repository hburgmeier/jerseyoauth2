package com.github.hburgmeier.jerseyoauth2.authsrv.openid;

import com.github.hburgmeier.jerseyoauth2.authsrv.api.user.IUserService;
import com.google.inject.AbstractModule;

public class OpenIdModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(IUserService.class).to(OpenIdUserService.class);
	}

}
