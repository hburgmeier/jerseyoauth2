package com.burgmeier.jerseyoauth2.api.user;

public interface IUser {

	String getName();
	
	boolean isUserInRole(String role);
	
}
