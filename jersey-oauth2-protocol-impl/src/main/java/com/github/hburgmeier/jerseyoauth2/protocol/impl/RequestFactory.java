package com.github.hburgmeier.jerseyoauth2.protocol.impl;

import java.util.EnumSet;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IHttpRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.IRequestFactory;
import com.github.hburgmeier.jerseyoauth2.api.protocol.IResourceAccessRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2Exception;
import com.github.hburgmeier.jerseyoauth2.api.types.ParameterStyle;
import com.github.hburgmeier.jerseyoauth2.api.types.TokenType;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.resourceaccess.ResourceAccessParser;

public class RequestFactory implements IRequestFactory {

	private final ResourceAccessParser resourceAccessParser = new ResourceAccessParser();
	
	@Override
	public IResourceAccessRequest parseResourceAccessRequest(IHttpRequest request,
			EnumSet<ParameterStyle> parameterStyles, EnumSet<TokenType> tokenTypes) throws OAuth2Exception {
		return resourceAccessParser.parse(request, parameterStyles, tokenTypes);
	}
}
