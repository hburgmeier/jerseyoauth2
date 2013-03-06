package com.burgmeier.jerseyoauth2.test;

import org.junit.Assert;
import org.junit.Test;
import org.scribe.model.Token;

import com.burgmeier.jerseyoauth2.test.client.ClientException;
import com.burgmeier.jerseyoauth2.test.client.TestClient;
import com.burgmeier.jerseyoauth2.testsuite.resource.SampleEntity;

public class ResourceTest extends BaseTest {

	@Test
	public void testInvalidResourceAccess()
	{
		TestClient client = new TestClient(clientEntity);
		try {
			client.sendTestRequestSample1(new Token("Invalid",""));
			Assert.fail();
		} catch (ClientException e) {
		}
	}
	
	@Test
	public void testValidResourceAccess() throws ClientException
	{
		String code = authClient.authorizeClient(clientEntity, "test1 test2").getCode();
		Assert.assertNotNull(code);
		restClient.setFollowRedirects(false);
		
		TestClient client = new TestClient(clientEntity);
		Token tok = client.getAccessToken(code);
		Assert.assertNotNull(tok);
		Assert.assertNotNull(tok.getToken());
		
		client.sendTestRequestSample1(tok);
		
		SampleEntity entity = client.retrieveEntitySample1(tok);
		Assert.assertNotNull(entity);
		Assert.assertEquals("manager", entity.getUsername());
		Assert.assertEquals(clientEntity.getClientId(), entity.getClientApp());
	}	
	
	@Test
	public void testClassAnnotation() throws ClientException
	{
		String code = authClient.authorizeClient(clientEntity, "").getCode();
		Assert.assertNotNull(code);
		restClient.setFollowRedirects(false);
		
		TestClient client = new TestClient(clientEntity);
		Token oldToken = client.getAccessToken(code);
		Assert.assertNotNull(oldToken);
		Assert.assertNotNull(oldToken.getToken());
		
		client.sendTestRequestSample2(oldToken);		
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
			client.sendTestRequestSample1(tok);
			Assert.fail();
		} catch (ClientException cex) {
		}
	}		
}
