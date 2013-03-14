package com.burgmeier.jerseyoauth2.rs.impl.context;

import java.security.Principal;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.burgmeier.jerseyoauth2.api.client.IAuthorizedClientApp;
import com.burgmeier.jerseyoauth2.api.user.IUser;
import com.burgmeier.jerseyoauth2.rs.api.IOAuthPrincipal;

public class OAuthPrincipal implements Principal, IOAuthPrincipal {

	private static final Logger logger = LoggerFactory.getLogger(OAuthPrincipal.class);
	
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
		logger.warn("isUserInRole called. This is not supported right now.");
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
