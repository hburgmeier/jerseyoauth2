package com.burgmeier.jerseyoauth2.test.client;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.burgmeier.jerseyoauth2.test.client.scribe.LocalTestAPI;
import com.burgmeier.jerseyoauth2.testsuite.resource.ClientEntity;

public class TestClient {
	
	private ClientEntity clientEntity;
	
	public TestClient(ClientEntity clientEntity) {
		super();
		this.clientEntity = clientEntity;
	}

	protected OAuthService getOAuthService()
	{
		OAuthService service = new ServiceBuilder()
		.provider(LocalTestAPI.class)
		.apiKey(clientEntity.getClientId())
		.apiSecret(clientEntity.getClientSecret())
	    .scope("test1 test2")
		.build();
		return service;
	}
	
	public String getAuthUrl()
	{
		OAuthService service = getOAuthService();
		return service.getAuthorizationUrl(null);
	}

	public Token getAccessToken(String code) {
		OAuthService service = getOAuthService();
		return service.getAccessToken(null, new Verifier(code));
	}	
	
	public String retrieveEntity(Token accessToken) throws ClientException
	{
		OAuthService service = getOAuthService();
		
		OAuthRequest request = new OAuthRequest(Verb.GET,
				"http://localhost:9998/testsuite/rest/sample/1");
		service.signRequest(accessToken, request);
		Response response = request.send();
		if (response.getCode()!=200)
			throw new ClientException(response.getBody());
		return response.getBody();
	}


	
}
