package com.github.hburgmeier.jerseyoauth2.protocol.impl;

import java.util.EnumSet;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IAccessTokenRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.IAuthorizationRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.IHttpRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.IRequestFactory;
import com.github.hburgmeier.jerseyoauth2.api.protocol.IResourceAccessRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2ParseException;
import com.github.hburgmeier.jerseyoauth2.api.types.ParameterStyle;
import com.github.hburgmeier.jerseyoauth2.api.types.TokenType;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.accesstoken.AbstractTokenRequest;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.accesstoken.AccessTokenRequestParser;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.authorization.AuthorizationRequest;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.authorization.AuthorizationRequestParser;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.resourceaccess.ResourceAccessParser;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.resourceaccess.ResourceAccessRequest;

public class RequestFactory implements IRequestFactory {

	private final ResourceAccessParser resourceAccessParser = new ResourceAccessParser();
	private final AuthorizationRequestParser authorizationRequestParser = new AuthorizationRequestParser();
	private final AccessTokenRequestParser accessTokenRequestParser = new AccessTokenRequestParser();
	
	@Override
	public IResourceAccessRequest parseResourceAccessRequest(IHttpRequest request,
			EnumSet<ParameterStyle> parameterStyles, EnumSet<TokenType> tokenTypes) throws OAuth2ParseException {
		ResourceAccessRequest accessRequest = resourceAccessParser.parse(request, parameterStyles, tokenTypes);
		accessRequest.validate();
		return accessRequest;
	}

	@Override
	public IAuthorizationRequest parseAuthorizationRequest(IHttpRequest request, boolean useAuthorizationHeader) throws OAuth2ParseException {
		AuthorizationRequest authzRequest = authorizationRequestParser.parse(request, useAuthorizationHeader);
		authzRequest.validate();
		return authzRequest;
	}
	
	@Override
	public IAccessTokenRequest parseAccessTokenRequest(IHttpRequest request, boolean useAuthorizationHeader) throws OAuth2ParseException {
		AbstractTokenRequest accessTokenRequest = accessTokenRequestParser.parse(request, useAuthorizationHeader);
		accessTokenRequest.validate();
		return accessTokenRequest;
	}
}
