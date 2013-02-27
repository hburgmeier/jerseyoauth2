package com.burgmeier.jerseyoauth2.api.client;

import java.util.Set;

import com.burgmeier.jerseyoauth2.api.user.IUser;

public interface IAuthorizedClientApp {

	String getClientId();
	
	IUser getAuthorizedUser();
	
	Set<String> getAuthorizedScopes();
	
	boolean isClientSecretValid(String clientSecret);
}
