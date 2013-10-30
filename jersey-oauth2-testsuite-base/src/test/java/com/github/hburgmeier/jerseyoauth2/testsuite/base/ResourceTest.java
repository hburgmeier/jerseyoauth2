package com.github.hburgmeier.jerseyoauth2.testsuite.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.scribe.model.Token;

import com.github.hburgmeier.jerseyoauth2.testsuite.base.client.ClientException;
import com.github.hburgmeier.jerseyoauth2.testsuite.base.client.ResourceClient;

public class ResourceTest extends BaseTest {

	@Test
	public void testInvalidResourceAccess()
	{
		ResourceClient client = new ResourceClient(clientEntity);
		try {
			client.sendTestRequestSample1(new Token("Invalid",""));
			fail();
		} catch (ClientException e) {
		}
	}
	
	@Test
	public void testValidResourceAccess() throws ClientException
	{
		String code = authClient.authorizeClient(clientEntity, "test1 test2").getCode();
		assertNotNull(code);
		restClient.setFollowRedirects(false);
		
		ResourceClient client = new ResourceClient(clientEntity);
		Token tok = client.getAccessToken(code);
		assertNotNull(tok);
		assertNotNull(tok.getToken());
		
		client.sendTestRequestSample1(tok);
		
		SampleEntity entity = client.retrieveEntitySample1(tok);
		assertNotNull(entity);
		assertEquals("manager", entity.getUsername());
		assertEquals(clientEntity.getClientId(), entity.getClientApp());
	}	
	
	@Test
	public void testClassAnnotation() throws ClientException
	{
		String code = authClient.authorizeClient(clientEntity, "").getCode();
		assertNotNull(code);
		restClient.setFollowRedirects(false);
		
		ResourceClient client = new ResourceClient(clientEntity);
		Token oldToken = client.getAccessToken(code);
		assertNotNull(oldToken);
		assertNotNull(oldToken.getToken());
		
		client.sendTestRequestSample2(oldToken);		
	}	
	
	@Test
	public void testInvalidScopes()
	{
		String code = authClient.authorizeClient(clientEntity, "test1 invalidScope").getCode();
		assertNotNull(code);
		restClient.setFollowRedirects(false);
		
		ResourceClient client = new ResourceClient(clientEntity);
		Token tok = client.getAccessToken(code);
		assertNotNull(tok);
		assertNotNull(tok.getToken());
		
		try {
			client.sendTestRequestSample1(tok);
			fail();
		} catch (ClientException cex) {
		}
	}	
	
	@Test
	public void testTokenExpiration() throws ClientException, InterruptedException
	{
		String code = authClient.authorizeClient(clientEntity, "test1 test2").getCode();
		assertNotNull(code);
		restClient.setFollowRedirects(false);
		
		ResourceClient client = new ResourceClient(clientEntity);
		Token tok = client.getAccessToken(code);
		assertNotNull(tok);
		assertNotNull(tok.getToken());
		
		client.sendTestRequestSample1(tok);
		
		Thread.sleep(7000);
		
		try {
			client.sendTestRequestSample1(tok);
			fail();
		} catch (ClientException e) {
		}
	}		
		
}
