package com.github.hburgmeier.jerseyoauth2.authsrv.api.token;


public interface IAccessTokenInfo extends com.github.hburgmeier.jerseyoauth2.api.token.IAccessTokenInfo {

	String getExpiresIn();

	String getRefreshToken();

	String getAccessToken();

	void updateTokens(String newAccessToken, String newRefreshToken);

	boolean isExpired();
	
}
