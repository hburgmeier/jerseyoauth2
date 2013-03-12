package com.burgmeier.jerseyoauth2.test;

import org.apache.amber.oauth2.common.message.types.GrantType;
import org.apache.amber.oauth2.common.message.types.ResponseType;
import org.junit.Assert;
import org.junit.Test;
import org.scribe.model.Token;

import com.burgmeier.jerseyoauth2.test.client.ClientException;
import com.burgmeier.jerseyoauth2.test.client.ResourceClient;
import com.burgmeier.jerseyoauth2.testsuite.resource.ClientEntity;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

// This test OAuth2 Spec draft 31 Chapter 4.2
public class ImplicitGrantTest extends BaseTest {

	protected ClientEntity registerClient() {
		return authClient.createClient("public");
	}		
	
	@Test
	public void testAccessToken()
	{
		String code = authClient.authorizeClient(clientEntity, "test1 test2").getCode();
		Assert.assertNotNull(code);
		restClient.setFollowRedirects(false);
		
		ResourceClient client = new ResourceClient(clientEntity.getClientId(), GrantType.AUTHORIZATION_CODE, ResponseType.TOKEN);
		String authUrl = client.getAuthUrl(null);
		
		WebResource webResource = restClient.resource(authUrl);
		ClientResponse clientResponse = webResource.get(ClientResponse.class);
		Assert.assertEquals(302, clientResponse.getStatus());
		String fragment = clientResponse.getLocation().getFragment();
		Assert.assertNotNull(fragment);
	}
	
	@Test
	public void testResourceAccess() throws ClientException
	{
		String code = authClient.authorizeClient(clientEntity, "test1 test2").getCode();
		Assert.assertNotNull(code);
		restClient.setFollowRedirects(false);
		
		ResourceClient client = new ResourceClient(clientEntity.getClientId(), GrantType.AUTHORIZATION_CODE, ResponseType.TOKEN);
		String authUrl = client.getAuthUrl(null);
		
		WebResource webResource = restClient.resource(authUrl);
		ClientResponse clientResponse = webResource.get(ClientResponse.class);
		Assert.assertEquals(302, clientResponse.getStatus());
		String fragment = clientResponse.getLocation().getFragment();
		Assert.assertNotNull(fragment);
		
		Token accessToken = client.parseFragment(fragment);
		client.sendTestRequestSample1(accessToken);
		
	}	
	
}
