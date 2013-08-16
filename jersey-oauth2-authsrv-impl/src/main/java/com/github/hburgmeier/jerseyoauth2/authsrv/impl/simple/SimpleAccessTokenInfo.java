package com.github.hburgmeier.jerseyoauth2.authsrv.impl.simple;

import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import com.github.hburgmeier.jerseyoauth2.api.types.TokenType;
import com.github.hburgmeier.jerseyoauth2.api.user.IUser;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IAuthorizedClientApp;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.IAccessTokenInfo;

public class SimpleAccessTokenInfo implements IAccessTokenInfo {
	
	private IAuthorizedClientApp clientApp;
	private Duration expiresIn;
	
	private String accessToken;
	private String refreshToken;
	private TokenType tokenType;
	private long validUntil;

	public SimpleAccessTokenInfo(String accessToken, String refreshToken, IAuthorizedClientApp clientApp, Duration expiresIn, TokenType tokenType) {
		super();
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.clientApp = clientApp;
		this.expiresIn = expiresIn;
		this.tokenType = tokenType;
		DateTime now = DateTime.now();
		this.validUntil = now.plus(expiresIn).getMillis();
	}

	@Override
	public IAuthorizedClientApp getClientApp() {
		return clientApp;
	}

	@Override
	public Set<String> getAuthorizedScopes() {
		return clientApp.getAuthorizedScopes();
	}

	@Override
	public IUser getUser() {
		return clientApp.getAuthorizedUser();
	}

	@Override
	public Long getExpiresIn() {
		return expiresIn.getMillis();
	}

	@Override
	public String getRefreshToken() {
		return refreshToken;
	}

	@Override
	public String getAccessToken() {
		return accessToken;
	}

	@Override
	public void updateTokens(String newAccessToken, String newRefreshToken) {
		this.accessToken = newAccessToken;
		this.refreshToken = newRefreshToken;
	}

	@Override
	public boolean isExpired() {
		return System.currentTimeMillis()>validUntil;
	}
	
	@Override
	public String getClientId() {
		return this.clientApp.getClientId();
	}

	@Override
	public TokenType getTokenType() {
		return tokenType;
	}
}
