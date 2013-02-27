package com.burgmeier.jerseyoauth2.api.simple;

import java.util.Set;

import com.burgmeier.jerseyoauth2.api.client.IAuthorizedClientApp;
import com.burgmeier.jerseyoauth2.api.token.IAccessTokenInfo;
import com.burgmeier.jerseyoauth2.api.user.IUser;

public class SimpleAccessTokenInfo implements IAccessTokenInfo {
	
	private IAuthorizedClientApp clientApp;
	private String expiresIn;
	
	private String accessToken;
	private String refreshToken;

	public SimpleAccessTokenInfo(String accessToken, String refreshToken, IAuthorizedClientApp clientApp, String expiresIn) {
		super();
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.clientApp = clientApp;
		this.expiresIn = expiresIn;
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
	public String getExpiresIn() {
		return expiresIn;
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

}
