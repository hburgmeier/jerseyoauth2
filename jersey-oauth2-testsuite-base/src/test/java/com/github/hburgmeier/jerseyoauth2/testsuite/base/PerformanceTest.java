package com.github.hburgmeier.jerseyoauth2.testsuite.base;

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.scribe.model.Token;

import com.github.hburgmeier.jerseyoauth2.testsuite.base.client.ClientManagerClient;
import com.github.hburgmeier.jerseyoauth2.testsuite.base.client.ResourceClient;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class PerformanceTest {

	protected static Token token;
	protected static ResourceClient client;
	protected static ClientEntity clientEntity;

	@BeforeClass
	public static void classSetup()
	{
		ClientConfig cc = new DefaultClientConfig();
		Client restClient = Client.create(cc);
		ClientManagerClient authClient = new ClientManagerClient(restClient);
		clientEntity = authClient.createClient("confidential");		
		
		String code = authClient.authorizeClient(clientEntity, "test1 test2").getCode();
		assertNotNull(code);
		restClient.setFollowRedirects(false);
		
		client = new ResourceClient(clientEntity);
		token = client.getAccessToken(code);
	}
	
}
