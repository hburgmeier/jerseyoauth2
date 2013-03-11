package com.burgmeier.jerseyoauth2.test;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.scribe.model.Token;

import com.burgmeier.jerseyoauth2.test.client.ClientException;
import com.burgmeier.jerseyoauth2.test.client.ClientManagerClient;
import com.burgmeier.jerseyoauth2.test.client.ResourceClient;
import com.burgmeier.jerseyoauth2.testsuite.resource.ClientEntity;
import com.burgmeier.jerseyoauth2.testsuite.resource.SampleEntity;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class PerformanceTest {

	@Rule
	public BenchmarkRule benchmarkRun = new BenchmarkRule();
	
	private static Token token;
	private static ResourceClient client;
	private static ClientEntity clientEntity;

	@BeforeClass
	public static void classSetup()
	{
		ClientConfig cc = new DefaultClientConfig();
		Client restClient = Client.create(cc);
		ClientManagerClient authClient = new ClientManagerClient(restClient);
		clientEntity = authClient.createClient();		
		
		String code = authClient.authorizeClient(clientEntity, "test1 test2").getCode();
		Assert.assertNotNull(code);
		restClient.setFollowRedirects(false);
		
		client = new ResourceClient(clientEntity);
		token = client.getAccessToken(code);
	}

	@BenchmarkOptions(benchmarkRounds=500)
	@Test
	public void testSimpleResourceAccess() throws ClientException
	{
		SampleEntity entity = client.retrieveEntitySample1(token);
		Assert.assertNotNull(entity);
		Assert.assertEquals("manager", entity.getUsername());
		Assert.assertEquals(clientEntity.getClientId(), entity.getClientApp());
	}
	
	@BenchmarkOptions(benchmarkRounds=200, concurrency=BenchmarkOptions.CONCURRENCY_AVAILABLE_CORES)
	@Test
	public void testParallelResourceAccess() throws ClientException
	{
		SampleEntity entity = client.retrieveEntitySample1(token);
		Assert.assertNotNull(entity);
		Assert.assertEquals("manager", entity.getUsername());
		Assert.assertEquals(clientEntity.getClientId(), entity.getClientApp());
	}	
	
}
