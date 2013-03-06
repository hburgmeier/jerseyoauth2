package com.burgmeier.jerseyoauth2.test;

import org.junit.Assert;
import org.junit.Test;
import org.scribe.model.Token;

import com.burgmeier.jerseyoauth2.client.scribe.OAuth2Token;
import com.burgmeier.jerseyoauth2.client.scribe.TokenExtractorException;
import com.burgmeier.jerseyoauth2.test.client.ClientException;
import com.burgmeier.jerseyoauth2.test.client.TestClient;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class OAuthTest extends BaseTest {
	
	@Test
	public void testAuthUrl()
	{
		String code = authClient.authorizeClient(clientEntity, "test1 test2").getCode();
		Assert.assertNotNull(code);
		restClient.setFollowRedirects(false);
		
		TestClient client = new TestClient(clientEntity);
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
	
}
