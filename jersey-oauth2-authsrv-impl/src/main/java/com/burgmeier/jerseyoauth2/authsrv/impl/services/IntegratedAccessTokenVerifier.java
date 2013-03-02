package com.burgmeier.jerseyoauth2.authsrv.impl.services;

import com.burgmeier.jerseyoauth2.api.token.IAccessTokenInfo;
import com.burgmeier.jerseyoauth2.api.token.InvalidTokenException;
import com.burgmeier.jerseyoauth2.authsrv.api.token.IAccessTokenStorageService;
import com.burgmeier.jerseyoauth2.rs.api.token.IAccessTokenVerifier;
import com.google.inject.Inject;

public class IntegratedAccessTokenVerifier implements IAccessTokenVerifier {

	private final IAccessTokenStorageService accessTokenStorageService;
	
	@Inject
	public IntegratedAccessTokenVerifier(final IAccessTokenStorageService accessTokenStorageService) {
		super();
		this.accessTokenStorageService = accessTokenStorageService;
	}

	@Override
	public IAccessTokenInfo verifyAccessToken(String accessToken) throws InvalidTokenException {
		return accessTokenStorageService.getTokenInfoByAccessToken(accessToken);
	}

}
