package com.burgmeier.jerseyoauth2.testsuite.resource;

import javax.xml.bind.annotation.XmlRootElement;

import com.burgmeier.jerseyoauth2.api.client.IClientAuthorization;

@XmlRootElement
public class ClientAuthEntity {

	private String code;

	public ClientAuthEntity()
	{
	}
	
	public ClientAuthEntity(IClientAuthorization clientAuthorization) {
		super();
		this.code = clientAuthorization.getCode();
	}
	
	public String getCode()
	{
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
