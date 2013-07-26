package com.github.hburgmeier.jerseyoauth2.rs.api.token;

import com.github.hburgmeier.jerseyoauth2.api.token.IAccessTokenInfo;
import com.github.hburgmeier.jerseyoauth2.api.token.InvalidTokenException;

public interface IAccessTokenVerifier {

	IAccessTokenInfo verifyAccessToken(String accessToken) throws InvalidTokenException;
	
}
