package com.burgmeier.jerseyoauth2.api;

import java.util.Set;

import com.burgmeier.jerseyoauth2.api.client.IAuthorizedClientApp;
import com.burgmeier.jerseyoauth2.api.user.IUser;

public interface IOAuthPrincipal {

	IUser getUser();
	
	IAuthorizedClientApp getClientApp();
	
	Set<String> getAllowedScopes();
	
}
