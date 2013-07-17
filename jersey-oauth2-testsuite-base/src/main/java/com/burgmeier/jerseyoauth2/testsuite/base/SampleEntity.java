package com.burgmeier.jerseyoauth2.testsuite.base;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SampleEntity {

	private String id;
	private String username;
	private String clientApp;
	
	public SampleEntity()
	{
		
	}
	
	public SampleEntity(String id, String username, String clientApp) {
		super();
		this.id = id;
		this.username = username;
		this.clientApp = clientApp;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getClientApp() {
		return clientApp;
	}

	public void setClientApp(String clientApp) {
		this.clientApp = clientApp;
	}

	
	
	
}
