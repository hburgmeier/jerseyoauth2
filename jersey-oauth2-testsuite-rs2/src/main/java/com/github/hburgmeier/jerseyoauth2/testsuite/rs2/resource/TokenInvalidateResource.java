package com.github.hburgmeier.jerseyoauth2.testsuite.rs2.resource;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.IAccessTokenStorageService;

@Path("/invalidateToken")
public class TokenInvalidateResource {

	private final IAccessTokenStorageService accessTokenService;

	@Inject
	public TokenInvalidateResource(IAccessTokenStorageService accessTokenService) {
		super();
		this.accessTokenService = accessTokenService;
	}

	@GET
	public String invalidateToken(@QueryParam("username") String username)
	{
		accessTokenService.invalidateTokensForUser(username);
		return "OK";
	}
}
