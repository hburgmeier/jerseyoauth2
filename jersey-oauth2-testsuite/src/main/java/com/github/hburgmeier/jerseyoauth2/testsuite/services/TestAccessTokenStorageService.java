package com.github.hburgmeier.jerseyoauth2.testsuite.services;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.github.hburgmeier.jerseyoauth2.api.client.IAuthorizedClientApp;
import com.github.hburgmeier.jerseyoauth2.api.token.InvalidTokenException;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.IConfiguration;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.IAccessTokenInfo;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.IAccessTokenStorageService;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.simple.SimpleAccessTokenInfo;
import com.google.inject.Inject;

public class TestAccessTokenStorageService implements IAccessTokenStorageService {

	private static final Map<String, IAccessTokenInfo> tokenStore = Collections.synchronizedMap(new HashMap<String, IAccessTokenInfo>());
	private static final Map<String, IAccessTokenInfo> refreshTokenStore = Collections.synchronizedMap(new HashMap<String, IAccessTokenInfo>());

	private String tokenExpiration;
	
	@Inject
	public TestAccessTokenStorageService(final IConfiguration configuration)
	{
		tokenExpiration = String.valueOf(configuration.getTokenExpiration());
	}
	
	@Override
	public IAccessTokenInfo getTokenInfoByAccessToken(String accessToken)
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
	public IAccessTokenInfo issueToken(String accessToken, String refreshToken, 
			IAuthorizedClientApp clientApp) {
		SimpleAccessTokenInfo tokenInfo = new SimpleAccessTokenInfo(accessToken, refreshToken, clientApp, tokenExpiration);
		tokenStore.put(accessToken, tokenInfo);
		refreshTokenStore.put(refreshToken, tokenInfo);
		return tokenInfo;
	}

	@Override
	public IAccessTokenInfo getTokenInfoByRefreshToken(String refreshToken) throws InvalidTokenException {
		if (refreshToken==null)
			throw new InvalidTokenException();
		
		IAccessTokenInfo info = refreshTokenStore.get(refreshToken);
		
		if (info==null)
		{
			throw new InvalidTokenException();
		}
		
		return info;
	}

	@Override
	public IAccessTokenInfo refreshToken(String oldAccessToken, String newAccessToken, String newRefreshToken) {
		IAccessTokenInfo tokenInfo = tokenStore.remove(oldAccessToken);
		refreshTokenStore.remove(tokenInfo.getRefreshToken());
		
		tokenInfo.updateTokens(newAccessToken, newRefreshToken);
		
		tokenStore.put(newAccessToken, tokenInfo);
		refreshTokenStore.put(newRefreshToken, tokenInfo);
		return tokenInfo;
	}
	
	@Override
	public List<IAccessTokenInfo> invalidateTokensForUser(String username) {
		List<IAccessTokenInfo> values = new LinkedList<>(tokenStore.values());
		for (IAccessTokenInfo tokenInfo : values)
		{
			tokenStore.remove(tokenInfo.getAccessToken());
			refreshTokenStore.remove(tokenInfo.getRefreshToken());
		}
		return values;
	}	

}
