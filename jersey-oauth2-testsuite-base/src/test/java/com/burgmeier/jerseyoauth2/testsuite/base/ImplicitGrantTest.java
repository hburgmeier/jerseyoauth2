package com.burgmeier.jerseyoauth2.testsuite.base;

import org.junit.Assert;
import org.junit.Test;
import org.scribe.model.Token;

import com.burgmeier.jerseyoauth2.api.types.GrantType;
import com.burgmeier.jerseyoauth2.api.types.ResponseType;
import com.burgmeier.jerseyoauth2.testsuite.base.client.ClientException;
import com.burgmeier.jerseyoauth2.testsuite.base.client.ResourceClient;
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
		
		ResourceClient client = new ResourceClient(clientEntity.getClientId(), GrantType.AUTHORIZATION_REQUEST, ResponseType.TOKEN);
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
		
		ResourceClient client = new ResourceClient(clientEntity.getClientId(), GrantType.AUTHORIZATION_REQUEST, ResponseType.TOKEN);
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
