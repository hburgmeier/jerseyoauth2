package com.burgmeier.jerseyoauth2.jerseyoauth2.testsuite.rs2.test;

import org.junit.Assert;
import org.junit.Test;
import org.scribe.model.Token;

import com.burgmeier.jerseyoauth2.client.scribe.OAuth2Token;
import com.burgmeier.jerseyoauth2.client.scribe.TokenExtractorException;
import com.burgmeier.jerseyoauth2.jerseyoauth2.testsuite.rs2.test.client.ClientException;
import com.burgmeier.jerseyoauth2.jerseyoauth2.testsuite.rs2.test.client.ResourceClient;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class ProtocolTest extends BaseTest {
	
	@Test
	public void testAuthUrl()
	{
		String code = authClient.authorizeClient(clientEntity, "test1 test2").getCode();
		Assert.assertNotNull(code);
		restClient.setFollowRedirects(false);
		
		ResourceClient client = new ResourceClient(clientEntity);
		String authUrl = client.getAuthUrl(null);
		
		WebResource webResource = restClient.resource(authUrl);
		ClientResponse clientResponse = webResource.get(ClientResponse.class);
		Assert.assertEquals(302, clientResponse.getStatus());
		Assert.assertTrue(clientResponse.getLocation().toString().startsWith("http://localhost:9998/example1?code="));
		
		authUrl = client.getAuthUrl("stateTest");
		
		webResource = restClient.resource(authUrl);
		clientResponse = webResource.get(ClientResponse.class);
		Assert.assertEquals(302, clientResponse.getStatus());
		Assert.assertTrue(clientResponse.getLocation().toString().contains("state=stateTest"));		
	}		
	
	@Test
	public void testInvalidClientSecret()
	{
		String code = authClient.authorizeClient(clientEntity, "test1 test2").getCode();
		Assert.assertNotNull(code);
		restClient.setFollowRedirects(false);
		
		ResourceClient client = new ResourceClient(clientEntity.getClientId(), "Invalid", "test1 test2");
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
		
		ResourceClient client = new ResourceClient(clientEntity);
		try {
			client.getAccessToken("A"+code);
			Assert.fail();
		} catch (TokenExtractorException e) {
		}		
	}	
	
	@Test
	public void testTokenInvalidation() throws ClientException
	{
		String code = authClient.authorizeClient(clientEntity, "test1 test2").getCode();
		Assert.assertNotNull(code);
		restClient.setFollowRedirects(false);
		
		ResourceClient client = new ResourceClient(clientEntity);
		Token token = client.getAccessToken(code);
		Assert.assertNotNull(token);
		Assert.assertNotNull(token.getToken());
		
		client.sendTestRequestSample1(token);
		
		authClient.invalidateToken("manager");
		
		try {
			client.sendTestRequestSample1(token);
			Assert.fail();
		} catch (ClientException e) {
		}
		
		try {
			client.refreshToken((OAuth2Token)token);
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
		
		ResourceClient client = new ResourceClient(clientEntity);
		Token oldToken = client.getAccessToken(code);
		Assert.assertNotNull(oldToken);
		Assert.assertNotNull(oldToken.getToken());
		
		client.sendTestRequestSample1(oldToken);
		
		Token newToken = client.refreshToken((OAuth2Token)oldToken);
		Assert.assertNotNull(newToken);
		
		client.sendTestRequestSample1(newToken);
		try {
			client.sendTestRequestSample1(oldToken);
			Assert.fail();
		} catch(ClientException ex) {
			
		}
		
	}
	
	@Test
	public void testRefreshTokenFlowExpires() throws ClientException, InterruptedException
	{
		String code = authClient.authorizeClient(clientEntity, "test1 test2").getCode();
		Assert.assertNotNull(code);
		restClient.setFollowRedirects(false);
		
		ResourceClient client = new ResourceClient(clientEntity);
		Token oldToken = client.getAccessToken(code);
		Assert.assertNotNull(oldToken);
		Assert.assertNotNull(oldToken.getToken());
		
		client.sendTestRequestSample1(oldToken);
		
		Thread.sleep(5000);
		
		try {
			client.refreshToken((OAuth2Token)oldToken);
			Assert.fail();
		} catch (TokenExtractorException e1) {
		}
	}	
	
	@Test
	public void testInvalidScopes()
	{
		String code = authClient.authorizeClient(clientEntity, "test1 someScope").getCode();
		Assert.assertNotNull(code);
		restClient.setFollowRedirects(false);
		
		ResourceClient client = new ResourceClient(clientEntity.getClientId(), clientEntity.getClientSecret(), "test1 test2");
		String authUrl = client.getAuthUrl(null);
		
		WebResource webResource = restClient.resource(authUrl);
		ClientResponse clientResponse = webResource.get(ClientResponse.class);
		Assert.assertEquals(200, clientResponse.getStatus());
	}	
	
}
