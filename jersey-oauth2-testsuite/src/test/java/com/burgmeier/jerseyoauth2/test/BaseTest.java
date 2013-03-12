package com.burgmeier.jerseyoauth2.test;

import org.junit.Before;

import com.burgmeier.jerseyoauth2.test.client.ClientManagerClient;
import com.burgmeier.jerseyoauth2.testsuite.resource.ClientEntity;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public abstract class BaseTest {

	protected Client restClient;
	protected ClientManagerClient authClient;
	protected ClientEntity clientEntity;
	
	@Before
	public void setUp()
	{
		ClientConfig cc = new DefaultClientConfig();
		restClient = Client.create(cc);
		authClient = new ClientManagerClient(restClient);
		clientEntity = registerClient();
	}

	protected ClientEntity registerClient() {
		return authClient.createClient("confidential");
	}	
	
}
