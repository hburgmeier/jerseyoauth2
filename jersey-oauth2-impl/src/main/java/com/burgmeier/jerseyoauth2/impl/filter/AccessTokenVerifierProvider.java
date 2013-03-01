package com.burgmeier.jerseyoauth2.impl.filter;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.burgmeier.jerseyoauth2.api.token.IAccessTokenVerifier;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
@Provider
public class AccessTokenVerifierProvider implements ContextResolver<IAccessTokenVerifier>{

	private final IAccessTokenVerifier accessTokenVerifier;

	@Inject
	public AccessTokenVerifierProvider(final IAccessTokenVerifier accessTokenVerifier)
	{
		this.accessTokenVerifier = accessTokenVerifier;
	}
	
	@Override
	public IAccessTokenVerifier getContext(Class<?> type) {
		return this.accessTokenVerifier;
	}


}
