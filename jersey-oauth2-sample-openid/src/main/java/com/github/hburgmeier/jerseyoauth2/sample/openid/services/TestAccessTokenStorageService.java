package com.github.hburgmeier.jerseyoauth2.sample.openid.services;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.joda.time.Duration;

import com.github.hburgmeier.jerseyoauth2.api.token.InvalidTokenException;
import com.github.hburgmeier.jerseyoauth2.api.types.TokenType;
import com.github.hburgmeier.jerseyoauth2.api.user.IUser;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.IConfiguration;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IAuthorizedClientApp;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.IAccessTokenInfo;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.IAccessTokenStorageService;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.simple.SimpleAccessTokenInfo;

public class TestAccessTokenStorageService implements IAccessTokenStorageService {

	private static final Map<String, IAccessTokenInfo> tokenStore = Collections.synchronizedMap(new HashMap<String, IAccessTokenInfo>());
	private static final Map<String, IAccessTokenInfo> refreshTokenStore = Collections.synchronizedMap(new HashMap<String, IAccessTokenInfo>());

	private Duration tokenExpiration;
	
	@Inject
	public TestAccessTokenStorageService(final IConfiguration configuration)
	{
		tokenExpiration = configuration.getTokenLifetime();
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
		SimpleAccessTokenInfo tokenInfo = new SimpleAccessTokenInfo(accessToken, refreshToken, clientApp, tokenExpiration, TokenType.BEARER);
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
	public List<IAccessTokenInfo> invalidateTokensForUser(IUser user) {
		List<IAccessTokenInfo> values = new LinkedList<>(tokenStore.values());
		for (IAccessTokenInfo tokenInfo : values)
		{
			tokenStore.remove(tokenInfo.getAccessToken());
			refreshTokenStore.remove(tokenInfo.getRefreshToken());
		}
		return values;
	}

}
