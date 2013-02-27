package com.burgmeier.jerseyoauth2.impl.filter;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.burgmeier.jerseyoauth2.api.token.IAccessTokenService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
@Provider
public class AccessTokenServiceProvider implements ContextResolver<IAccessTokenService>{

	private final IAccessTokenService accessTokenService;

	@Inject
	public AccessTokenServiceProvider(final IAccessTokenService accessTokenService)
	{
		this.accessTokenService = accessTokenService;
		
	}
	
	@Override
	public IAccessTokenService getContext(Class<?> type) {
		return this.accessTokenService;
	}


}
