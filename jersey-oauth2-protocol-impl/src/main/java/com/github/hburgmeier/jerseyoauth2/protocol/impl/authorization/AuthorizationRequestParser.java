package com.github.hburgmeier.jerseyoauth2.protocol.impl.authorization;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IHttpRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2Exception;
import com.github.hburgmeier.jerseyoauth2.api.types.ResponseType;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.ClientSecretExtractor;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.ScopeParser;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.extractor.CombinedExtractor;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.oauth2.Constants;

public class AuthorizationRequestParser {
	
	private final ScopeParser scopeParser = new ScopeParser();
	
	private final CombinedExtractor responseTypeExtractor = new CombinedExtractor(Constants.RESPONSE_TYPE);
	private final CombinedExtractor stateExtractor = new CombinedExtractor(Constants.STATE);
	private final CombinedExtractor clientIdExtractor = new CombinedExtractor(Constants.CLIENT_ID);
	private final CombinedExtractor scopeExtractor = new CombinedExtractor(Constants.SCOPE);
	private final CombinedExtractor redirectUriExtractor = new CombinedExtractor(Constants.REDIRECT_URI);

	public AuthorizationRequest parse(IHttpRequest request, boolean enableAuthorizationHeader) throws OAuth2Exception {
		String state = stateExtractor.extractValue(request);
		String responseTypeString = responseTypeExtractor.extractValue(request);
		if (StringUtils.isEmpty(responseTypeString))
			//TODO
			throw new OAuth2Exception();
		
		ResponseType responseType = ResponseType.valueOf(responseTypeString.toUpperCase());
		String clientId = clientIdExtractor.extractValue(request);
		String redirectUri = redirectUriExtractor.extractValue(request);
		String scope = scopeExtractor.extractValue(request);
		Set<String> scopes = scopeParser.parseScope(scope);
		
		ClientSecretExtractor clientSecretExtractor = new ClientSecretExtractor(enableAuthorizationHeader);
		String clientSecret = clientSecretExtractor.extractValue(request);
		
		return new AuthorizationRequest(responseType, clientId, clientSecret, redirectUri, scopes, state);
	}

}
