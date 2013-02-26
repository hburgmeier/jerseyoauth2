package com.burgmeier.jerseyoauth2.impl.context;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

public class OAuthSecurityContext implements SecurityContext {

	private OAuthPrincipal principal;
	
	public OAuthSecurityContext(OAuthPrincipal principal) {
		super();
		this.principal = principal;
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getAuthenticationScheme() {
		// TODO Auto-generated method stub
		return null;
	}

}
