package com.github.hburgmeier.jerseyoauth2.authsrv.jpa.guice;

import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IClientService;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.IAccessTokenStorageService;
import com.github.hburgmeier.jerseyoauth2.authsrv.jpa.CachingAccessTokenStorage;
import com.github.hburgmeier.jerseyoauth2.authsrv.jpa.DatabaseClientService;
import com.google.inject.AbstractModule;

public class PersistenceModule extends AbstractModule {

	@Override
	protected void configure() {
    	bind(IClientService.class).to(DatabaseClientService.class);
    	bind(IAccessTokenStorageService.class).to(CachingAccessTokenStorage.class);
	}

}
