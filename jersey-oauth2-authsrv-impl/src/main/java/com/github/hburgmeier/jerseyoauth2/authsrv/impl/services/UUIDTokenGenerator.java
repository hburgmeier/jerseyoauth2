package com.github.hburgmeier.jerseyoauth2.authsrv.impl.services;

import java.util.UUID;

import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.ITokenGenerator;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.TokenGenerationException;

public class UUIDTokenGenerator implements ITokenGenerator {

	@Override
	public String createAccessToken() throws TokenGenerationException {
		return generateValue();
	}

	@Override
	public String createRefreshToken() throws TokenGenerationException {
		return generateValue();
	}

	@Override
	public String createAuthenticationCode() throws TokenGenerationException {
		return generateValue();
	}
	
	protected String generateValue()
	{
		return UUID.randomUUID().toString();
	}

}
