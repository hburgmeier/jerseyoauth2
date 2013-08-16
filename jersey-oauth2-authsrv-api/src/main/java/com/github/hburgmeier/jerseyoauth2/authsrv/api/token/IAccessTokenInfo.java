package com.github.hburgmeier.jerseyoauth2.authsrv.api.token;

import com.github.hburgmeier.jerseyoauth2.api.types.TokenType;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IAuthorizedClientApp;


public interface IAccessTokenInfo extends com.github.hburgmeier.jerseyoauth2.api.token.IAccessTokenInfo {

	IAuthorizedClientApp getClientApp();
	
	Long getExpiresIn();

	String getRefreshToken();

	String getAccessToken();
	
	TokenType getTokenType();

	void updateTokens(String newAccessToken, String newRefreshToken);

	boolean isExpired();
	
}
