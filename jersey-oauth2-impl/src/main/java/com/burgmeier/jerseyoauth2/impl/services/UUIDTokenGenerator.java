package com.burgmeier.jerseyoauth2.impl.services;

import org.apache.amber.oauth2.as.issuer.UUIDValueGenerator;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;

import com.burgmeier.jerseyoauth2.api.token.ITokenGenerator;

public class UUIDTokenGenerator implements ITokenGenerator {

	private UUIDValueGenerator vg = new UUIDValueGenerator();
	
	@Override
	public String createAccessToken() throws OAuthSystemException {
		return vg.generateValue();
	}

	@Override
	public String createRefreshToken() throws OAuthSystemException {
		return vg.generateValue();
	}

	@Override
	public String createAuthenticationCode() throws OAuthSystemException {
		return vg.generateValue();
	}

}
