package com.burgmeier.jerseyoauth2.api.simple;

import java.io.Serializable;

import com.burgmeier.jerseyoauth2.api.client.IRegisteredClientApp;

public class SimpleRegisteredClient implements IRegisteredClientApp, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String clientId;
	private String clientSecret;
	private String appName;
	
	public SimpleRegisteredClient()
	{
		
	}
	
	public SimpleRegisteredClient(String clientId, String clientSecret, String appName) {
		super();
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.appName = appName;
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
		return appName;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	
	public void setApplicationName(String appName) {
		this.appName = appName;
	}

	
	
}
