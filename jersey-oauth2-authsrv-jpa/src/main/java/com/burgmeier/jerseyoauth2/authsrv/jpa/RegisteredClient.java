package com.burgmeier.jerseyoauth2.authsrv.jpa;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.burgmeier.jerseyoauth2.authsrv.api.client.ClientType;
import com.burgmeier.jerseyoauth2.authsrv.api.client.IRegisteredClientApp;

@Entity
class RegisteredClient implements IRegisteredClientApp {

	@Id
	private String clientId;
	private String clientSecret;
	private String applicationName;
	@NotNull
	private String callbackUrl;
	private ClientType clientType;

	public RegisteredClient()
	{
		
	}
	
	public RegisteredClient(String clientId, String clientSecret)
	{
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.clientType = ClientType.CONFIDENTIAL;
	}
	
	@Override
	public String getClientId() {
		return clientId;
	}

	@Override
	public String getClientSecret() {
		return clientSecret;
	}

	@Override
	public String getApplicationName() {
		return applicationName;
	}

	@Override
	public String getCallbackUrl() {
		return callbackUrl;
	}

	@Override
	public ClientType getClientType() {
		return clientType;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public void setClientType(ClientType clientType) {
		this.clientType = clientType;
	}
	
}
