package com.burgmeier.jerseyoauth2.test.client;

import javax.ws.rs.core.MediaType;

import com.burgmeier.jerseyoauth2.testsuite.resource.ClientAuthEntity;
import com.burgmeier.jerseyoauth2.testsuite.resource.ClientEntity;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

public class ClientManagerClient {

	private Client client;

	public ClientManagerClient(Client client) {
		super();
		this.client = client;
	}
	
	public ClientEntity createClient(String clientType)
	{
		WebResource webResource = client.resource("http://localhost:9998/testsuite/rest/clients");
		ClientEntity clientEntity = webResource.queryParam("appname", "UnitTest").
				queryParam("callbackUrl","http://localhost:9998/example1").
				queryParam("clientType",clientType).
				accept(MediaType.APPLICATION_JSON_TYPE).post(ClientEntity.class);
		return clientEntity;
	}
	
	public ClientAuthEntity authorizeClient(ClientEntity clientEntity, String scope)
	{
		client.addFilter(new HTTPBasicAuthFilter("manager", "test".getBytes()));
		WebResource webResource = client.resource("http://localhost:9998/testsuite/rest/clientAuth");
		ClientAuthEntity clientAuthEntity = webResource.
				queryParam("client_id", clientEntity.getClientId()).
				queryParam("scope", scope).
				post(ClientAuthEntity.class);
		return clientAuthEntity;
	}
	
	public void invalidateToken(String username)
	{
		WebResource webResource = client.resource("http://localhost:9998/testsuite/rest/invalidateToken");
		ClientResponse response = webResource.
				queryParam("username", username).
				get(ClientResponse.class);
		if (response.getStatus()!=204)
			throw new IllegalArgumentException(Integer.toString(response.getStatus()));
	}	
}
