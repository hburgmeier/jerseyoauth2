package com.burgmeier.jerseyoauth2.api.token;

import com.burgmeier.jerseyoauth2.api.client.IAuthorizedClientApp;

public interface IAccessTokenService {

	IAccessTokenInfo getAccessTokenInfo(String accessToken) throws InvalidTokenException;

	IAccessTokenInfo issueToken(String accessToken, IAuthorizedClientApp clientApp);
	
}
