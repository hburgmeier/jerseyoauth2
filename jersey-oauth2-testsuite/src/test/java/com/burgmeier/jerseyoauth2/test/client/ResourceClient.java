package com.burgmeier.jerseyoauth2.test.client;

import java.io.IOException;

import org.apache.amber.oauth2.common.message.types.GrantType;
import org.apache.amber.oauth2.common.message.types.ResponseType;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.burgmeier.jerseyoauth2.client.scribe.IOAuth2Service;
import com.burgmeier.jerseyoauth2.client.scribe.OAuth2Token;
import com.burgmeier.jerseyoauth2.testsuite.resource.ClientEntity;
import com.burgmeier.jerseyoauth2.testsuite.resource.SampleEntity;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResourceClient {
	
	private String clientId;
	private String clientSecret;
	private final GrantType grantType;
	private final ResponseType responseType;
	
	public ResourceClient(ClientEntity clientEntity) {
		super();
		this.clientId = clientEntity.getClientId();
		this.clientSecret = clientEntity.getClientSecret();
		this.grantType = GrantType.AUTHORIZATION_CODE;
		this.responseType = ResponseType.CODE;
	}
	
	public ResourceClient(String clientId, String clientSecret)
	{
		super();
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.grantType = GrantType.AUTHORIZATION_CODE;
		this.responseType = ResponseType.CODE;
	}
	
	public ResourceClient(String clientId, GrantType grantType, ResponseType responseType)
	{
		super();
		this.clientId = clientId;
		this.grantType = grantType;
		this.responseType = responseType;
	}	

	protected OAuthService getOAuthService(String scope, String state)
	{
		return getOAuthService(grantType, scope, state);
	}
	
	protected OAuthService getOAuthService(GrantType _grantType, String scope, String state)
	{
		ServiceBuilder serviceBuilder = new ServiceBuilder()
		.provider(new TestsuiteAPI(_grantType.toString(), state, responseType.toString()))
		.apiKey(clientId)
		.apiSecret(clientSecret==null?"DUMMYDUMMYDUMMY":clientSecret);
		
		if (scope!=null)
			serviceBuilder = serviceBuilder.scope(scope);
		OAuthService service = serviceBuilder.build();
		return service;
	}
	
	public String getAuthUrl(String state)
	{
		OAuthService service = getOAuthService("test1 test2", state);
		return service.getAuthorizationUrl(null);
	}

	public Token getAccessToken(String code) {
		OAuthService service = getOAuthService("test1 test2", null);
		return service.getAccessToken(null, new Verifier(code));
	}
	
	public Token refreshToken(OAuth2Token token) {
		OAuthService service = getOAuthService(GrantType.REFRESH_TOKEN, "test1 test2", null);
		return ((IOAuth2Service)service).refreshToken(token);
	}		
	
	public String sendTestRequestSample1(Token accessToken) throws ClientException
	{
		OAuthService service = getOAuthService("test1 test2", null);
		
		OAuthRequest request = new OAuthRequest(Verb.GET,
				"http://localhost:9998/testsuite/rest/sample/1");
		service.signRequest(accessToken, request);
		Response response = request.send();
		if (response.getCode()!=200)
			throw new ClientException(response.getBody());
		return response.getBody();
	}
	
	public SampleEntity retrieveEntitySample1(Token accessToken) throws ClientException
	{
		OAuthService service = getOAuthService("test1 test2", null);
		
		OAuthRequest request = new OAuthRequest(Verb.GET,
				"http://localhost:9998/testsuite/rest/sample/1");
		service.signRequest(accessToken, request);
		Response response = request.send();
		if (response.getCode()!=200)
			throw new ClientException(response.getBody());
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			SampleEntity entity = mapper.readValue(response.getBody(), SampleEntity.class);
			return entity;
		} catch (IOException e) {
			throw new ClientException(response.getBody());
		}
	}	
	
	public String sendTestRequestSample2(Token accessToken) throws ClientException
	{
		OAuthService service = getOAuthService(null, null);
		
		OAuthRequest request = new OAuthRequest(Verb.GET,
				"http://localhost:9998/testsuite/rest/sample2/1");
		service.signRequest(accessToken, request);
		Response response = request.send();
		if (response.getCode()!=200)
			throw new ClientException(response.getBody());
		return response.getBody();
	}
	
	public Token parseFragment(String fragment)
	{
		IOAuth2Service service = (IOAuth2Service)getOAuthService(null, null);
		return service.parseFragment(fragment);
		
	}


	
}
