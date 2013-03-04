package com.burgmeier.jerseyoauth2.rs.impl.context;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

public class OAuthSecurityContext implements SecurityContext {

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
		return null; // the token request is indeed not authorized
	}

}
