package com.github.hburgmeier.jerseyoauth2.authsrv.openid;

import java.io.Serializable;

import com.github.hburgmeier.jerseyoauth2.api.user.IUser;

public class OpenIDUser implements IUser, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String email;
	
	public OpenIDUser(String email) {
		super();
		this.email = email;
	}

	@Override
	public String getName() {
		return email;
	}

	@Override
	public boolean isUserInRole(String role) {
		return false;
	}

}
