package com.burgmeier.jerseyoauth2.authsrv.jpa;

import com.burgmeier.jerseyoauth2.api.user.IUser;

public class User implements IUser {

	private String name;
	
	public User(String name) {
		super();
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isUserInRole(String role) {
		return false;
	}

}
