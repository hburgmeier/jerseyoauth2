package com.github.hburgmeier.jerseyoauth2.api.protocol;

import java.util.EnumSet;

import com.github.hburgmeier.jerseyoauth2.api.types.ParameterStyle;
import com.github.hburgmeier.jerseyoauth2.api.types.TokenType;

public interface IRequestFactory {

	IResourceAccessRequest parseResourceAccessRequest(IHttpRequest request, EnumSet<ParameterStyle> parameterStyles, EnumSet<TokenType> tokenTypes)
		throws OAuth2ParseException;

	IAuthorizationRequest parseAuthorizationRequest(IHttpRequest request, boolean useAuthorizationHeader)
		throws OAuth2ParseException;

	IAccessTokenRequest parseAccessTokenRequest(IHttpRequest request, boolean useAuthorizationHeader)
		throws OAuth2ParseException;
	
}
