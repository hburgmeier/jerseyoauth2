package com.burgmeier.jerseyoauth2.authsrv.api.token;

import com.burgmeier.jerseyoauth2.api.client.IAuthorizedClientApp;
import com.burgmeier.jerseyoauth2.api.token.IAccessTokenInfo;
import com.burgmeier.jerseyoauth2.api.token.InvalidTokenException;

public interface IAccessTokenStorageService {

	IAccessTokenInfo getTokenInfoByAccessToken(String accessToken) throws InvalidTokenException;

	IAccessTokenInfo issueToken(String accessToken, String refreshToken, IAuthorizedClientApp clientApp);
	
	IAccessTokenInfo getTokenInfoByRefreshToken(String refreshToken) throws InvalidTokenException;
	
	IAccessTokenInfo refreshToken(String oldAccessToken, String newAccessToken, String newRefreshToken);
	
}
