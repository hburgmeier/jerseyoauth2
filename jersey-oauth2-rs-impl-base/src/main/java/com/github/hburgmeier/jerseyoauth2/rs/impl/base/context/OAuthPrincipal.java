package com.github.hburgmeier.jerseyoauth2.rs.impl.base.context;

import java.security.Principal;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hburgmeier.jerseyoauth2.api.user.IUser;
import com.github.hburgmeier.jerseyoauth2.rs.api.IOAuthPrincipal;

public class OAuthPrincipal implements Principal, IOAuthPrincipal {

	private static final Logger LOGGER = LoggerFactory.getLogger(OAuthPrincipal.class);
	
	private IUser user;
	private String clientId;
	private Set<String> allowedScopes;
	
	public OAuthPrincipal(String clientId, IUser user, Set<String> allowedScopes) {
		super();
		this.clientId = clientId;
		this.user = user;
		this.allowedScopes = allowedScopes;
	}

	@Override
	public String getName() {
		return user.getName();
	}

	public boolean isUserInRole(String role) {
		LOGGER.warn("isUserInRole called. This is not supported right now.");
		return false;
	}

	@Override
	public IUser getUser() {
		return user;
	}

	@Override
	public String getClientId() {
		return clientId;
	}

	@Override
	public Set<String> getAllowedScopes() {
		return allowedScopes;
	}

}
