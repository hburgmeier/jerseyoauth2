package com.burgmeier.jerseyoauth2.test.client;

import javax.ws.rs.core.MediaType;

import com.burgmeier.jerseyoauth2.testsuite.resource.ClientAuthEntity;
import com.burgmeier.jerseyoauth2.testsuite.resource.ClientEntity;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

public class AuthClient {

	private Client client;

	public AuthClient(Client client) {
		super();
		this.client = client;
	}
	
	public ClientEntity createClient()
	{
		WebResource webResource = client.resource("http://localhost:9998/example1/rest/clients");
		ClientEntity clientEntity = webResource.queryParam("appname", "UnitTest").accept(MediaType.APPLICATION_JSON_TYPE).post(ClientEntity.class);
		return clientEntity;
	}
	
	public ClientAuthEntity authorizeClient(ClientEntity clientEntity)
	{
		client.addFilter(new HTTPBasicAuthFilter("manager", "test".getBytes()));
		WebResource webResource = client.resource("http://localhost:9998/example1/rest/clientAuth");
		ClientAuthEntity clientAuthEntity = webResource.
				queryParam("client_id", clientEntity.getClientId()).
				queryParam("scope","test1 test2").
				post(ClientAuthEntity.class);
		return clientAuthEntity;
	}
}
