package com.github.hburgmeier.jerseyoauth2.testsuite.base;

import org.junit.Before;

import com.github.hburgmeier.jerseyoauth2.testsuite.base.ClientEntity;
import com.github.hburgmeier.jerseyoauth2.testsuite.base.client.ClientManagerClient;
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
