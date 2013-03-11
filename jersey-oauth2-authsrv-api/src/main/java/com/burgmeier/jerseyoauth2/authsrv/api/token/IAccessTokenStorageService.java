package com.burgmeier.jerseyoauth2.authsrv.api.token;

import java.util.List;

import com.burgmeier.jerseyoauth2.api.client.IAuthorizedClientApp;
import com.burgmeier.jerseyoauth2.api.token.IAccessTokenInfo;
import com.burgmeier.jerseyoauth2.api.token.InvalidTokenException;

public interface IAccessTokenStorageService {

	IAccessTokenInfo getTokenInfoByAccessToken(String accessToken) throws InvalidTokenException;

	IAccessTokenInfo issueToken(String accessToken, String refreshToken, IAuthorizedClientApp clientApp) throws TokenStorageException;
	
	IAccessTokenInfo getTokenInfoByRefreshToken(String refreshToken) throws InvalidTokenException;
	
	IAccessTokenInfo refreshToken(String oldAccessToken, String newAccessToken, String newRefreshToken) throws TokenStorageException;
	
	List<IAccessTokenInfo> invalidateTokensForUser(String username);
	
}
