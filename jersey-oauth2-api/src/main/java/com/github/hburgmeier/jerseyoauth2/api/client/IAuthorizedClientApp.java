package com.github.hburgmeier.jerseyoauth2.api.client;

import java.util.Set;

import com.github.hburgmeier.jerseyoauth2.api.user.IUser;

public interface IAuthorizedClientApp {

	String getClientId();
	
	String getCallbackUrl();	
	
	IUser getAuthorizedUser();
	
	Set<String> getAuthorizedScopes();
	
	boolean isClientSecretValid(String clientSecret);
}
