package com.burgmeier.jerseyoauth2.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.scribe.model.Token;

import com.burgmeier.jerseyoauth2.client.scribe.OAuth2Token;
import com.burgmeier.jerseyoauth2.client.scribe.TokenExtractorException;
import com.burgmeier.jerseyoauth2.test.client.ClientException;
import com.burgmeier.jerseyoauth2.test.client.ClientManagerClient;
import com.burgmeier.jerseyoauth2.test.client.TestClient;
import com.burgmeier.jerseyoauth2.testsuite.resource.ClientEntity;
import com.burgmeier.jerseyoauth2.testsuite.resource.SampleEntity;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class OAuthTest {

	private Client restClient;
	private ClientManagerClient authClient;
	private ClientEntity clientEntity;
	
	@Before
	public void setUp()
	{
		ClientConfig cc = new DefaultClientConfig();
		restClient = Client.create(cc);
		authClient = new ClientManagerClient(restClient);
		clientEntity = authClient.createClient();
	}
	
	@Test
	public void testAuthUrl()
	{
		String code = authClient.authorizeClient(clientEntity, "test1 test2").getCode();
		Assert.assertNotNull(code);
		restClient.setFollowRedirects(false);
		
		TestClient client = new TestClient(clientEntity);
		String authUrl = client.getAuthUrl();
		
		WebResource webResource = restClient.resource(authUrl);
		ClientResponse clientResponse = webResource.get(ClientResponse.class);
		Assert.assertEquals(302, clientResponse.getStatus());
		Assert.assertTrue(clientResponse.getLocation().toString().startsWith("http://localhost:9998/example1?code="));
	}		
	
	@Test
	public void testResourceAccess() throws ClientException
	{
		String code = authClient.authorizeClient(clientEntity, "test1 test2").getCode();
		Assert.assertNotNull(code);
		restClient.setFollowRedirects(false);
		
		TestClient client = new TestClient(clientEntity);
		Token tok = client.getAccessToken(code);
		Assert.assertNotNull(tok);
		Assert.assertNotNull(tok.getToken());
		
		client.sendTestRequest(tok);
		
		SampleEntity entity = client.retrieveEntity(tok);
		Assert.assertNotNull(entity);
		Assert.assertEquals("manager", entity.getUsername());
		Assert.assertEquals(clientEntity.getClientId(), entity.getClientApp());
	}
	
	@Test
	public void testInvalidResourceAccess()
	{
		TestClient client = new TestClient(clientEntity);
		try {
			client.sendTestRequest(new Token("Invalid",""));
			Assert.fail();
		} catch (ClientException e) {
		}
	}
	
	@Test
	public void testInvalidScopes()
	{
		String code = authClient.authorizeClient(clientEntity, "test1 invalidScope").getCode();
		Assert.assertNotNull(code);
		restClient.setFollowRedirects(false);
		
		TestClient client = new TestClient(clientEntity);
		Token tok = client.getAccessToken(code);
		Assert.assertNotNull(tok);
		Assert.assertNotNull(tok.getToken());
		
		try {
			client.sendTestRequest(tok);
			Assert.fail();
		} catch (ClientException cex) {
		}
	}	
	
	@Test
	public void testInvalidClientSecret()
	{
		String code = authClient.authorizeClient(clientEntity, "test1 test2").getCode();
		Assert.assertNotNull(code);
		restClient.setFollowRedirects(false);
		
		TestClient client = new TestClient(clientEntity.getClientId(),"Invalid");
		try {
			client.getAccessToken(code);
			Assert.fail();
		} catch (TokenExtractorException e) {
		}
	}	
	
	@Test
	public void testInvalidCode()
	{
		String code = authClient.authorizeClient(clientEntity, "test1 test2").getCode();
		Assert.assertNotNull(code);
		restClient.setFollowRedirects(false);
		
		TestClient client = new TestClient(clientEntity);
		try {
			client.getAccessToken("A"+code);
			Assert.fail();
		} catch (TokenExtractorException e) {
		}		
	}		
	
	@Test
	public void testRefreshTokenFlow() throws ClientException
	{
		String code = authClient.authorizeClient(clientEntity, "test1 test2").getCode();
		Assert.assertNotNull(code);
		restClient.setFollowRedirects(false);
		
		TestClient client = new TestClient(clientEntity);
		Token oldToken = client.getAccessToken(code);
		Assert.assertNotNull(oldToken);
		Assert.assertNotNull(oldToken.getToken());
		
		client.sendTestRequest(oldToken);
		
		Token newToken = client.refreshToken((OAuth2Token)oldToken);
		Assert.assertNotNull(newToken);
		
		client.sendTestRequest(newToken);
		try {
			client.sendTestRequest(oldToken);
			Assert.fail();
		} catch(ClientException ex) {
			
		}
		
	}
	
}
