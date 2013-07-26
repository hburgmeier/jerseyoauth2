package com.github.hburgmeier.jerseyoauth2.authsrv.api.token;

import org.apache.amber.oauth2.common.exception.OAuthSystemException;

public interface ITokenGenerator {

	String createAccessToken() throws OAuthSystemException;
	
	String createRefreshToken() throws OAuthSystemException;
	
	String createAuthenticationCode() throws OAuthSystemException;
	
}
