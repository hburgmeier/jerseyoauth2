package com.burgmeier.jerseyoauth2.authsrv.impl.services;

import javax.inject.Inject;

import com.burgmeier.jerseyoauth2.api.token.IAccessTokenInfo;
import com.burgmeier.jerseyoauth2.api.token.InvalidTokenException;
import com.burgmeier.jerseyoauth2.authsrv.api.token.IAccessTokenStorageService;
import com.burgmeier.jerseyoauth2.rs.api.token.IAccessTokenVerifier;

public class IntegratedAccessTokenVerifier implements IAccessTokenVerifier {

	private final IAccessTokenStorageService accessTokenStorageService;
	
	@Inject
	public IntegratedAccessTokenVerifier(IAccessTokenStorageService accessTokenStorageService) {
		super();
		this.accessTokenStorageService = accessTokenStorageService;
	}

	@Override
	public IAccessTokenInfo verifyAccessToken(String accessToken) throws InvalidTokenException {
		return accessTokenStorageService.getTokenInfoByAccessToken(accessToken);
	}

}
