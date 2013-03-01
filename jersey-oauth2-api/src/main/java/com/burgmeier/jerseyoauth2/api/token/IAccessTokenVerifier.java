package com.burgmeier.jerseyoauth2.api.token;

public interface IAccessTokenVerifier {

	IAccessTokenInfo verifyAccessToken(String accessToken) throws InvalidTokenException;
	
}
