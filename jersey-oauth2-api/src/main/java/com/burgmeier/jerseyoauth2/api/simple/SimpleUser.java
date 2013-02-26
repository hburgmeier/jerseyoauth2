package com.burgmeier.jerseyoauth2.api.simple;

import java.security.Principal;

import com.burgmeier.jerseyoauth2.api.user.IUser;

public class SimpleUser implements IUser {

	private Principal principal;

	public SimpleUser(Principal principal) {
		super();
		this.principal = principal;
	}

	@Override
	public String getName() {
		return principal.getName();
	}

	@Override
	public boolean isUserInRole(String role) {
		return false;
	}
	
	
	
}
