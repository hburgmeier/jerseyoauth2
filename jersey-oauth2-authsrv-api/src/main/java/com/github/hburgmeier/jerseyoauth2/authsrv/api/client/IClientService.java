package com.github.hburgmeier.jerseyoauth2.authsrv.api.client;

import java.util.Set;

import com.github.hburgmeier.jerseyoauth2.api.user.IUser;

public interface IClientService {

	IRegisteredClientApp registerClient(String appName, String callbackUrl, ClientType clientType) throws ClientServiceException;
	
	IRegisteredClientApp getRegisteredClient(String clientId);	
	
	IAuthorizedClientApp authorizeClient(IUser user, IRegisteredClientApp clientApp, Set<String> allowedScopes) throws ClientServiceException;
	
	IAuthorizedClientApp isAuthorized(IUser user, String clientId, Set<String> scopes);
	
	IPendingClientToken createPendingClientToken(IAuthorizedClientApp clientApp) throws ClientServiceException;
	
	IPendingClientToken findPendingClientToken(String clientId, String clientSecret, String code);

	void removePendingClientToken(IPendingClientToken pendingClientToken);
	
	void removePendingTokensForUser(IUser user);

}
