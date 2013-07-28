package com.github.hburgmeier.jerseyoauth2.protocol.impl;

import java.util.EnumSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IHttpRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.IRequestFactory;
import com.github.hburgmeier.jerseyoauth2.api.protocol.IResourceAccessRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2Exception;
import com.github.hburgmeier.jerseyoauth2.api.types.ParameterStyle;
import com.github.hburgmeier.jerseyoauth2.api.types.TokenType;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.extractor.HeaderExtractor;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.extractor.IExtractor;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.extractor.QueryParameterExtractor;

public class RequestFactory implements IRequestFactory {

	private static final Pattern authPattern = Pattern.compile("([a-zA-Z]+) (.+)");
	
	@Override
	public IResourceAccessRequest parseResourceAccessRequest(IHttpRequest request,
			EnumSet<ParameterStyle> parameterStyles, EnumSet<TokenType> tokenTypes) throws OAuth2Exception {
		String accessToken = null;
		for (TokenType tokenType : tokenTypes)
		{
			for (ParameterStyle parameterStyle : parameterStyles)
			{
				IExtractor extractor = parameterStyle==ParameterStyle.HEADER?
						new HeaderExtractor(HttpHeaders.AUTHORIZATION):new QueryParameterExtractor("access_token");
				String authorization = extractor.extractValue(request);
				if (authorization!=null)
				{
					if (parameterStyle==ParameterStyle.HEADER)
					{
					Matcher mat = authPattern.matcher(authorization);
					if (mat.matches() && mat.group(1).equalsIgnoreCase(tokenType.toString()))
					{
						accessToken = mat.group(2);
					}
					} else
						accessToken = authorization;
				}
			}
		}
		if (accessToken == null)
			throw new OAuth2Exception("Missing access token");
		ResourceAccessRequest accessRequest = new ResourceAccessRequest(accessToken);
		accessRequest.validate();
		return accessRequest;
	}
}
