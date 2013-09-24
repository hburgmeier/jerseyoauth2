package com.github.hburgmeier.jerseyoauth2.protocol.impl.authorization;

import java.util.Collections;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IAuthorizationRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2ParseException;
import com.github.hburgmeier.jerseyoauth2.api.types.ResponseType;

public class AuthorizationRequest implements IAuthorizationRequest {

	private final ResponseType responseType;
	private final String clientId;
	private final String redirectUri;
	private final Set<String> scopes;
	private final String state;
	
	public AuthorizationRequest(ResponseType responseType, String clientId, String redirectUri,
			Set<String> scopes, String state) {
		super();
		this.responseType = responseType;
		this.clientId = clientId;
		this.redirectUri = redirectUri;
		this.scopes = scopes;
		this.state = state;
	}

	@Override
	public String getClientId() {
		return clientId;
	}

	@Override
	public ResponseType getResponseType() {
		return responseType;
	}

	@Override
	public Set<String> getScopes() {
		return scopes==null?null:Collections.unmodifiableSet(scopes);
	}

	@Override
	public String getRedirectURI() {
		return redirectUri;
	}
	
	@Override
	public String getState() {
		return state;
	}
	
	public void validate() throws OAuth2ParseException {
		if (responseType == null) {
			throw new OAuth2ParseException("Missing response_type", state);
		}
		if (StringUtils.isEmpty(clientId)) {
			throw new OAuth2ParseException("Missing client id", state);
		}
	}

}
