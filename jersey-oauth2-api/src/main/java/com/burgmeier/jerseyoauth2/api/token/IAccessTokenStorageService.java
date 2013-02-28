package com.burgmeier.jerseyoauth2.api.token;

import com.burgmeier.jerseyoauth2.api.client.IAuthorizedClientApp;

public interface IAccessTokenStorageService {

	IAccessTokenInfo getAccessTokenInfo(String accessToken) throws InvalidTokenException;

	IAccessTokenInfo issueToken(String accessToken, String refreshToken, IAuthorizedClientApp clientApp);
	
	IAccessTokenInfo getTokenInfoByRefreshToken(String refreshToken) throws InvalidTokenException;
	
	IAccessTokenInfo refreshToken(String oldAccessToken, String newAccessToken, String newRefreshToken);
	
}
