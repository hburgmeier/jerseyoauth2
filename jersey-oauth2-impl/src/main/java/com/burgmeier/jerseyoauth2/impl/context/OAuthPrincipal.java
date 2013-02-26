package com.burgmeier.jerseyoauth2.impl.context;

import java.security.Principal;
import java.util.Set;

import com.burgmeier.jerseyoauth2.api.IOAuthPrincipal;
import com.burgmeier.jerseyoauth2.api.client.IAuthorizedClientApp;
import com.burgmeier.jerseyoauth2.api.user.IUser;

public class OAuthPrincipal implements Principal, IOAuthPrincipal {

	private IUser user;
	private IAuthorizedClientApp clientApp;
	private Set<String> allowedScopes;
	
	public OAuthPrincipal(IAuthorizedClientApp clientApp, IUser user, Set<String> allowedScopes) {
		super();
		this.clientApp = clientApp;
		this.user = user;
		this.allowedScopes = allowedScopes;
	}

	@Override
	public String getName() {
		return user.getName();
	}

	public boolean isUserInRole(String role) {
		return user.isUserInRole(role);
	}

	@Override
	public IUser getUser() {
		return user;
	}

	@Override
	public IAuthorizedClientApp getClientApp() {
		return clientApp;
	}

	@Override
	public Set<String> getAllowedScopes() {
		return allowedScopes;
	}

}
