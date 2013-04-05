package com.burgmeier.jerseyoauth2.rs.impl.base.context;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OAuthSecurityContext implements SecurityContext {

	private static final Logger logger = LoggerFactory.getLogger(OAuthSecurityContext.class);
	
	private OAuthPrincipal principal;
	private boolean secure;
	
	public OAuthSecurityContext(OAuthPrincipal principal, boolean secure) {
		super();
		this.principal = principal;
		this.secure = secure;
	}

	@Override
	public Principal getUserPrincipal() {
		return principal;
	}

	@Override
	public boolean isUserInRole(String role) {
		return principal.isUserInRole(role);
	}

	@Override
	public boolean isSecure() {
		return secure;
	}

	@Override
	public String getAuthenticationScheme() {
		logger.warn("getAuthenticationScheme called. This is not supported nor necessary.");
		return null; // the token request is indeed not authorized
	}

}
