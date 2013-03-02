package com.burgmeier.jerseyoauth2.rs.api.token;

import com.burgmeier.jerseyoauth2.api.token.IAccessTokenInfo;
import com.burgmeier.jerseyoauth2.api.token.InvalidTokenException;

public interface IAccessTokenVerifier {

	IAccessTokenInfo verifyAccessToken(String accessToken) throws InvalidTokenException;
	
}
