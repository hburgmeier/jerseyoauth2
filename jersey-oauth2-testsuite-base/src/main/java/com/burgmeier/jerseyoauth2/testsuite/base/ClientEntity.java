package com.burgmeier.jerseyoauth2.testsuite.base;

import javax.xml.bind.annotation.XmlRootElement;

import com.burgmeier.jerseyoauth2.authsrv.api.client.IRegisteredClientApp;

@XmlRootElement
public class ClientEntity {

	private String clientId;
	private String clientSecret;
	
	public ClientEntity()
	{
		
	}
	
	public ClientEntity(IRegisteredClientApp registeredClient)
	{
		this.clientId = registeredClient.getClientId();
		this.clientSecret = registeredClient.getClientSecret();
	}
	
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	
}
