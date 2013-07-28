package com.github.hburgmeier.jerseyoauth2.protocol.impl;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IResourceAccessRequest;

public class ResourceAccessRequest implements IResourceAccessRequest {

	protected String accessToken;
	
	public ResourceAccessRequest(String accessToken) {
		super();
		this.accessToken = accessToken;
	}
	
	public void validate()
	{
		//TODO
	}

	@Override
	public String getAccessToken() {
		return accessToken;
	}

}
