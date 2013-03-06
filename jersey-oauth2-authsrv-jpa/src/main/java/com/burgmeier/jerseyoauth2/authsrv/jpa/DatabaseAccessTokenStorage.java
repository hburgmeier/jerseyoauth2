package com.burgmeier.jerseyoauth2.authsrv.jpa;

import com.burgmeier.jerseyoauth2.api.client.IAuthorizedClientApp;
import com.burgmeier.jerseyoauth2.api.token.IAccessTokenInfo;
import com.burgmeier.jerseyoauth2.api.token.InvalidTokenException;
import com.burgmeier.jerseyoauth2.authsrv.api.token.IAccessTokenStorageService;

public class DatabaseAccessTokenStorage implements IAccessTokenStorageService {

	@Override
	public IAccessTokenInfo getTokenInfoByAccessToken(String accessToken) throws InvalidTokenException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IAccessTokenInfo issueToken(String accessToken, String refreshToken, IAuthorizedClientApp clientApp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IAccessTokenInfo getTokenInfoByRefreshToken(String refreshToken) throws InvalidTokenException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IAccessTokenInfo refreshToken(String oldAccessToken, String newAccessToken, String newRefreshToken) {
		// TODO Auto-generated method stub
		return null;
	}

}
