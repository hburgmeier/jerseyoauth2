package com.burgmeier.jerseyoauth2.authsrv.jpa;

import java.util.Set;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.burgmeier.jerseyoauth2.api.client.IAuthorizedClientApp;
import com.burgmeier.jerseyoauth2.api.user.IUser;

@Entity
public class AuthorizedClientApplication implements IAuthorizedClientApp {

	@Id
	private String id;
	
	@ManyToOne
	private RegisteredClient clientApp;

	public AuthorizedClientApplication()
	{
		this.id = UUID.randomUUID().toString();
	}
	
	@Override
	public String getClientId() {
		return clientApp.getClientId();
	}

	@Override
	public IUser getAuthorizedUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getAuthorizedScopes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isClientSecretValid(String clientSecret) {
		return clientApp.getClientSecret().equals(clientSecret);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RegisteredClient getClientApp() {
		return clientApp;
	}

	public void setClientApp(RegisteredClient clientApp) {
		this.clientApp = clientApp;
	}
	
}
