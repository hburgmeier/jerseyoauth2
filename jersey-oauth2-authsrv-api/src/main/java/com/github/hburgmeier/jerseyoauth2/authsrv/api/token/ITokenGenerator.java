package com.github.hburgmeier.jerseyoauth2.authsrv.api.token;


public interface ITokenGenerator {

	String createAccessToken() throws TokenGenerationException;
	
	String createRefreshToken() throws TokenGenerationException;
	
	String createAuthenticationCode() throws TokenGenerationException;
	
}
