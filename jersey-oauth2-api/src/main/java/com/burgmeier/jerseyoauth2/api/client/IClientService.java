package com.burgmeier.jerseyoauth2.api.client;

import java.util.Set;

import com.burgmeier.jerseyoauth2.api.user.IUser;

public interface IClientService {

	IRegisteredClientApp registerClient(String appName, String callbackUrl) throws ClientServiceException;
	
	IRegisteredClientApp getRegisteredClient(String clientId);	
	
	IAuthorizedClientApp findPendingClient(String clientId, String clientSecret, String code);

	IAuthorizedClientApp authorizeClient(IUser user, IRegisteredClientApp clientApp, Set<String> allowedScopes) throws ClientServiceException;
	
	IClientAuthorization isAuthorized(IUser user, String clientId, Set<String> scopes);
	
	IClientAuthorization createClientAuthorization(IAuthorizedClientApp clientApp) throws ClientServiceException;

}
