package com.burgmeier.jerseyoauth2.impl.filter;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.burgmeier.jerseyoauth2.api.token.IAccessTokenStorageService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
@Provider
public class AccessTokenServiceProvider implements ContextResolver<IAccessTokenStorageService>{

	private final IAccessTokenStorageService accessTokenService;

	@Inject
	public AccessTokenServiceProvider(final IAccessTokenStorageService accessTokenService)
	{
		this.accessTokenService = accessTokenService;
		
	}
	
	@Override
	public IAccessTokenStorageService getContext(Class<?> type) {
		return this.accessTokenService;
	}


}
