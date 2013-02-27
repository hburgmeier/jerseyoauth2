package com.burgmeier.jerseyoauth2.api.token;

import java.util.Set;

import com.burgmeier.jerseyoauth2.api.client.IAuthorizedClientApp;
import com.burgmeier.jerseyoauth2.api.user.IUser;

public interface IAccessTokenInfo {

	IAuthorizedClientApp getClientApp();

	IUser getUser();	
	
	Set<String> getAuthorizedScopes();

	String getExpiresIn();

	String getRefreshToken();

	String getAccessToken();

	void updateTokens(String newAccessToken, String newRefreshToken);
	
}
