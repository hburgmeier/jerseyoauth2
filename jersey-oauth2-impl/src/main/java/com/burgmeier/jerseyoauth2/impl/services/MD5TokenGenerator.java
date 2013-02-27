package com.burgmeier.jerseyoauth2.impl.services;

import org.apache.amber.oauth2.as.issuer.MD5Generator;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;

import com.burgmeier.jerseyoauth2.api.token.ITokenGenerator;

public class MD5TokenGenerator implements ITokenGenerator {

	private MD5Generator vg = new MD5Generator();
	
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
