package com.github.hburgmeier.jerseyoauth2.authsrv.api.simple;

import java.util.Set;

import com.github.hburgmeier.jerseyoauth2.api.token.IAccessTokenInfo;
import com.github.hburgmeier.jerseyoauth2.api.user.IUser;

public class SimpleAccessTokenInfo implements IAccessTokenInfo {
	
	private final Set<String> authorizedScopes;
	private final IUser user;
	private final String clientId;

	public SimpleAccessTokenInfo(String clientId, Set<String> authorizedScopes, IUser user) {
		super();
		this.clientId = clientId;
		this.authorizedScopes = authorizedScopes;
		this.user = user;
	}

	@Override
	public Set<String> getAuthorizedScopes() {
		return authorizedScopes;
	}

	@Override
	public IUser getUser() {
		return user;
	}

	@Override
	public String getClientId() {
		return clientId;
	}	
	
}
