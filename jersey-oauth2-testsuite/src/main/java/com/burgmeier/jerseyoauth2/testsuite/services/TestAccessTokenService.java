package com.burgmeier.jerseyoauth2.testsuite.services;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.burgmeier.jerseyoauth2.api.IConfiguration;
import com.burgmeier.jerseyoauth2.api.client.IAuthorizedClientApp;
import com.burgmeier.jerseyoauth2.api.simple.SimpleAccessTokenInfo;
import com.burgmeier.jerseyoauth2.api.token.IAccessTokenInfo;
import com.burgmeier.jerseyoauth2.api.token.IAccessTokenService;
import com.burgmeier.jerseyoauth2.api.token.InvalidTokenException;
import com.google.inject.Inject;

public class TestAccessTokenService implements IAccessTokenService {

	private static final Map<String, IAccessTokenInfo> tokenStore = Collections.synchronizedMap(new HashMap<String, IAccessTokenInfo>());

	private String tokenExpiration;
	
	@Inject
	public TestAccessTokenService(final IConfiguration configuration)
	{
		tokenExpiration = String.valueOf(configuration.getTokenExpiration());
	}
	
	@Override
	public IAccessTokenInfo getAccessTokenInfo(String accessToken)
			throws InvalidTokenException {
		
		if (accessToken==null)
			throw new InvalidTokenException();
		
		IAccessTokenInfo info = tokenStore.get(accessToken);
		
		if (info==null)
		{
			throw new InvalidTokenException();
		}
		
		return info;
	}

	@Override
	public IAccessTokenInfo issueToken(String accessToken,
			IAuthorizedClientApp clientApp) {
		SimpleAccessTokenInfo tokenInfo = new SimpleAccessTokenInfo(clientApp, tokenExpiration);
		tokenStore.put(accessToken, tokenInfo);
		return tokenInfo;
	}

}
