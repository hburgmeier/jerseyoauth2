package com.burgmeier.jerseyoauth2.authsrv.impl.simple;

import java.util.Set;

import com.burgmeier.jerseyoauth2.api.client.IAuthorizedClientApp;
import com.burgmeier.jerseyoauth2.api.user.IUser;
import com.burgmeier.jerseyoauth2.authsrv.api.client.IRegisteredClientApp;

public class SimpleAuthorizedClientApp implements IAuthorizedClientApp {

	private Set<String> allowedScopes;
	private IUser user;
	private IRegisteredClientApp registeredClient;
	
	public SimpleAuthorizedClientApp(IRegisteredClientApp registeredClient, IUser user, Set<String> allowedScopes) {
		super();
		this.registeredClient = registeredClient;
		this.user = user;
		this.allowedScopes = allowedScopes;
	}

	@Override
	public Set<String> getAuthorizedScopes() {
		return allowedScopes;
	}

	@Override
	public IUser getAuthorizedUser() {
		return user;
	}

	@Override
	public String getClientId() {
		return registeredClient.getClientId();
	}

	@Override
	public boolean isClientSecretValid(String clientSecret) {
		return registeredClient.getClientSecret().equals(clientSecret);
	}

}
