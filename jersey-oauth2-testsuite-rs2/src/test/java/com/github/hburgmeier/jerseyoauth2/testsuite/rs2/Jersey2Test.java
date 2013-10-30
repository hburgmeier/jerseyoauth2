package com.github.hburgmeier.jerseyoauth2.testsuite.rs2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.filter.HttpBasicAuthFilter;
import org.glassfish.jersey.client.oauth2.ClientIdentifier;
import org.glassfish.jersey.client.oauth2.OAuth2ClientSupport;
import org.glassfish.jersey.client.oauth2.OAuth2CodeGrantFlow;
import org.glassfish.jersey.client.oauth2.OAuth2CodeGrantFlow.Builder;
import org.glassfish.jersey.client.oauth2.TokenResult;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.Before;
import org.junit.Test;

import com.github.hburgmeier.jerseyoauth2.testsuite.base.BaseTest;
import com.github.hburgmeier.jerseyoauth2.testsuite.base.SampleEntity;

public class Jersey2Test extends BaseTest {

	
	protected Client client;
	
	@Before
	public void setUp()
	{
		super.setUp();
		ClientConfig clientConfig = new ClientConfig();
		clientConfig.property(ClientProperties.FOLLOW_REDIRECTS, false);
		client = ClientBuilder.newClient(clientConfig);
		client.register(new HttpBasicAuthFilter("manager", "test"));
		client.register(LoggingFilter.class);
		authClient.authorizeClient(clientEntity, "test1 test2");
	}
	
	@Test
	public void simpleResourceTest() throws UnsupportedEncodingException
	{
		
		ClientIdentifier cliendId = new ClientIdentifier(clientEntity.getClientId(), clientEntity.getClientSecret());
		
		Builder<?> flowBuilder = OAuth2ClientSupport.authorizationCodeGrantFlowBuilder(cliendId, "http://localhost:9998/testsuite/oauth2/auth", "http://localhost:9998/testsuite/oauth2/accessToken");
		OAuth2CodeGrantFlow flow = flowBuilder.scope("test1 test2").redirectUri("http://localhost:9998/testsuite").build();
		String authUrl = flow.start();
		
		Map<String, String> map = retrieveCode(authUrl);
		String code = map.get("code");
		String state = map.get("state"); 
		
		TokenResult tokenResult = flow.finish(code, state);

		client = ClientBuilder.newClient();
		client.register(LoggingFilter.class);
		client.register(OAuth2ClientSupport.feature(tokenResult.getAccessToken()));
		client.register(JacksonFeature.class);
		Invocation.Builder builder = client.target("http://localhost:9998/testsuite/rest/sample/1").request();
		Response response = builder.get();
		
		assertEquals(200, response.getStatus());
		SampleEntity entity = response.readEntity(SampleEntity.class);
		assertNotNull(entity);
	}

	protected Map<String, String> retrieveCode(String authUrl) throws UnsupportedEncodingException {
		Invocation.Builder request = client.target(authUrl).request();
		Response response = request.get();
		URI location = response.getLocation();
		return splitQuery(location.getQuery());
	}
	
	protected static Map<String, String> splitQuery(String query) throws UnsupportedEncodingException {
	    Map<String, String> query_pairs = new LinkedHashMap<String, String>();
	    String[] pairs = query.split("&");
	    for (String pair : pairs) {
	        int idx = pair.indexOf("=");
	        query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
	    }
	    return query_pairs;
	}	
}
