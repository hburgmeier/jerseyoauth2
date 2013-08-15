package com.github.hburgmeier.jerseyoauth2.testsuite.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.scribe.model.Token;

import com.github.hburgmeier.jerseyoauth2.client.scribe.OAuth2Token;
import com.github.hburgmeier.jerseyoauth2.client.scribe.TokenExtractorException;
import com.github.hburgmeier.jerseyoauth2.testsuite.base.client.ClientException;
import com.github.hburgmeier.jerseyoauth2.testsuite.base.client.ResourceClient;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class ProtocolTest extends BaseTest {
	
	@Test
	public void testAuthUrl()
	{
		String code = authClient.authorizeClient(clientEntity, "test1 test2").getCode();
		assertNotNull(code);
		restClient.setFollowRedirects(false);
		
		ResourceClient client = new ResourceClient(clientEntity);
		String authUrl = client.getAuthUrl(null);
		
		WebResource webResource = restClient.resource(authUrl);
		ClientResponse clientResponse = webResource.get(ClientResponse.class);
		assertEquals(302, clientResponse.getStatus());
		assertTrue(clientResponse.getLocation().toString().startsWith("http://localhost:9998/testsuite?code="));
		
		authUrl = client.getAuthUrl("stateTest");
		
		webResource = restClient.resource(authUrl);
		clientResponse = webResource.get(ClientResponse.class);
		assertEquals(302, clientResponse.getStatus());
		assertTrue(clientResponse.getLocation().toString().contains("state=stateTest"));		
	}		
	
	@Test
	public void testInvalidClientSecret()
	{
		String code = authClient.authorizeClient(clientEntity, "test1 test2").getCode();
		assertNotNull(code);
		restClient.setFollowRedirects(false);
		
		ResourceClient client = new ResourceClient(clientEntity.getClientId(), "Invalid", "test1 test2");
		try {
			client.getAccessToken(code);
			fail();
		} catch (TokenExtractorException e) {
		}
	}	
	
	@Test
	public void testInvalidCode()
	{
		String code = authClient.authorizeClient(clientEntity, "test1 test2").getCode();
		assertNotNull(code);
		restClient.setFollowRedirects(false);
		
		ResourceClient client = new ResourceClient(clientEntity);
		try {
			client.getAccessToken("A"+code);
			fail();
		} catch (TokenExtractorException e) {
		}		
	}	
	
	@Test
	public void testDoubleUseOfCode()
	{
		String code = authClient.authorizeClient(clientEntity, "test1 test2").getCode();
		assertNotNull(code);
		restClient.setFollowRedirects(false);
		
		ResourceClient client = new ResourceClient(clientEntity);
		client.getAccessToken(code);
		try {
			client.getAccessToken(code);
			fail();
		} catch (TokenExtractorException e) {
		}		
	}	
	
	@Test
	public void testTokenInvalidation() throws ClientException
	{
		String code = authClient.authorizeClient(clientEntity, "test1 test2").getCode();
		assertNotNull(code);
		restClient.setFollowRedirects(false);
		
		ResourceClient client = new ResourceClient(clientEntity);
		Token token = client.getAccessToken(code);
		assertNotNull(token);
		assertNotNull(token.getToken());
		
		client.sendTestRequestSample1(token);
		
		authClient.invalidateToken("manager");
		
		try {
			client.sendTestRequestSample1(token);
			fail();
		} catch (ClientException e) {
		}
		
		try {
			client.refreshToken((OAuth2Token)token);
			fail();
		} catch (TokenExtractorException e) {
		}
	}		
	
	@Test
	public void testRefreshTokenFlow() throws ClientException
	{
		String code = authClient.authorizeClient(clientEntity, "test1 test2").getCode();
		assertNotNull(code);
		restClient.setFollowRedirects(false);
		
		ResourceClient client = new ResourceClient(clientEntity);
		Token oldToken = client.getAccessToken(code);
		assertNotNull(oldToken);
		assertNotNull(oldToken.getToken());
		
		client.sendTestRequestSample1(oldToken);
		
		Token newToken = client.refreshToken((OAuth2Token)oldToken);
		assertNotNull(newToken);
		
		client.sendTestRequestSample1(newToken);
		try {
			client.sendTestRequestSample1(oldToken);
			fail();
		} catch(ClientException ex) {
			
		}
		
	}
	
	@Test
	public void testRefreshTokenFlowExpires() throws ClientException, InterruptedException
	{
		String code = authClient.authorizeClient(clientEntity, "test1 test2").getCode();
		assertNotNull(code);
		restClient.setFollowRedirects(false);
		
		ResourceClient client = new ResourceClient(clientEntity);
		Token oldToken = client.getAccessToken(code);
		assertNotNull(oldToken);
		assertNotNull(oldToken.getToken());
		
		client.sendTestRequestSample1(oldToken);
		
		Thread.sleep(7000);
		
		try {
			client.refreshToken((OAuth2Token)oldToken);
			fail();
		} catch (TokenExtractorException e1) {
		}
	}	
	
	@Test
	public void testInvalidScopes()
	{
		String code = authClient.authorizeClient(clientEntity, "test1 someScope").getCode();
		assertNotNull(code);
		restClient.setFollowRedirects(false);
		
		ResourceClient client = new ResourceClient(clientEntity.getClientId(), clientEntity.getClientSecret(), "test1 test2");
		String authUrl = client.getAuthUrl(null);
		
		WebResource webResource = restClient.resource(authUrl);
		ClientResponse clientResponse = webResource.get(ClientResponse.class);
		assertEquals(302, clientResponse.getStatus());
		assertTrue(clientResponse.getLocation().toString().startsWith("http://localhost:9998/testsuite?code="));
	}	
	
}
