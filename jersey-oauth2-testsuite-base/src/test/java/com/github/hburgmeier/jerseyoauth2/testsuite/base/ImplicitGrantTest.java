package com.github.hburgmeier.jerseyoauth2.testsuite.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.scribe.model.Token;

import com.github.hburgmeier.jerseyoauth2.api.types.GrantType;
import com.github.hburgmeier.jerseyoauth2.api.types.ResponseType;
import com.github.hburgmeier.jerseyoauth2.testsuite.base.client.ClientException;
import com.github.hburgmeier.jerseyoauth2.testsuite.base.client.ResourceClient;
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
		assertNotNull(code);
		restClient.setFollowRedirects(false);
		
		ResourceClient client = new ResourceClient(clientEntity.getClientId(), GrantType.AUTHORIZATION_REQUEST, ResponseType.TOKEN);
		String authUrl = client.getAuthUrl(null);
		
		WebResource webResource = restClient.resource(authUrl);
		ClientResponse clientResponse = webResource.get(ClientResponse.class);
		assertEquals(302, clientResponse.getStatus());
		String fragment = clientResponse.getLocation().getFragment();
		assertNotNull(fragment);
	}

	@Test
	public void testNoRefreshToken()
	{
		String code = authClient.authorizeClient(clientEntity, "test1 test2").getCode();
		assertNotNull(code);
		restClient.setFollowRedirects(false);
		
		ResourceClient client = new ResourceClient(clientEntity.getClientId(), GrantType.AUTHORIZATION_REQUEST, ResponseType.TOKEN);
		String authUrl = client.getAuthUrl(null);
		
		WebResource webResource = restClient.resource(authUrl);
		ClientResponse clientResponse = webResource.get(ClientResponse.class);
		assertEquals(302, clientResponse.getStatus());
		String fragment = clientResponse.getLocation().getFragment();
		assertNotNull(fragment);
		assertTrue(!fragment.contains("refresh_token"));
	}	
	
	@Test
	public void testResourceAccess() throws ClientException
	{
		String code = authClient.authorizeClient(clientEntity, "test1 test2").getCode();
		assertNotNull(code);
		restClient.setFollowRedirects(false);
		
		ResourceClient client = new ResourceClient(clientEntity.getClientId(), GrantType.AUTHORIZATION_REQUEST, ResponseType.TOKEN);
		String authUrl = client.getAuthUrl(null);
		
		WebResource webResource = restClient.resource(authUrl);
		ClientResponse clientResponse = webResource.get(ClientResponse.class);
		assertEquals(302, clientResponse.getStatus());
		String fragment = clientResponse.getLocation().getFragment();
		assertNotNull(fragment);
		
		Token accessToken = client.parseFragment(fragment);
		client.sendTestRequestSample1(accessToken);
		
	}	
	
	@Test
	public void testAuthCodeWithImplicit() throws ClientException
	{
		String code = authClient.authorizeClient(clientEntity, "test1 test2").getCode();
		assertNotNull(code);
		restClient.setFollowRedirects(false);
		
		ResourceClient client = new ResourceClient(clientEntity.getClientId(), GrantType.AUTHORIZATION_REQUEST, ResponseType.CODE);
		String authUrl = client.getAuthUrl(null);
		
		WebResource webResource = restClient.resource(authUrl);
		ClientResponse clientResponse = webResource.get(ClientResponse.class);
		assertTrue(clientResponse.getLocation().toString().startsWith("http://localhost:9998/testsuite?error=unsupported_response_type"));
	}
}
