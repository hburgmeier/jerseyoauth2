package com.github.hburgmeier.jerseyoauth2.protocol.impl.resourceaccess;

import java.util.EnumSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IHttpRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.IResourceAccessRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2Exception;
import com.github.hburgmeier.jerseyoauth2.api.types.ParameterStyle;
import com.github.hburgmeier.jerseyoauth2.api.types.TokenType;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.HttpHeaders;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.ResourceAccessRequest;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.extractor.FormExtractor;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.extractor.HeaderExtractor;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.extractor.IExtractor;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.extractor.QueryParameterExtractor;

public class ResourceAccessParser {
	
	public IResourceAccessRequest parse(IHttpRequest request,
			EnumSet<ParameterStyle> parameterStyles, EnumSet<TokenType> tokenTypes) throws OAuth2Exception {
		String accessToken = null;
		for (TokenType tokenType : tokenTypes)
		{
			for (ParameterStyle parameterStyle : parameterStyles)
			{
				IExtractor extractor = getAccessTokenExtractor(tokenType, parameterStyle);
				accessToken = extractor.extractValue(request);
				if (accessToken!=null)
					break;
			}
		}
		if (accessToken == null)
			throw new OAuth2Exception("Missing access token");
		ResourceAccessRequest accessRequest = new ResourceAccessRequest(accessToken);
		accessRequest.validate();
		return accessRequest;
	}
	
	protected IExtractor getAccessTokenExtractor(TokenType tokenType, ParameterStyle parameterStyle)
	{
		switch (parameterStyle)
		{
		case HEADER:
			return new AccessTokenHeaderExtractor(tokenType);
		case QUERY:
			return new QueryParameterExtractor("access_token");
		case BODY:
			return new FormExtractor("access_token");
		}
		throw new IllegalArgumentException(parameterStyle.toString());
	}
	
	private static class AccessTokenHeaderExtractor extends HeaderExtractor {
		private static final Pattern authPattern = Pattern.compile("([a-zA-Z]+) (.+)");
		private TokenType tokenType;

		public AccessTokenHeaderExtractor(TokenType tokenType) {
			super(HttpHeaders.AUTHORIZATION);
			this.tokenType = tokenType;
		}
		
		@Override
		public String extractValue(IHttpRequest request) {
			String value = super.extractValue(request);
			String accessToken = null;
			if (StringUtils.isNotEmpty(value))
			{
				Matcher mat = authPattern.matcher(value);
				if (mat.matches() && mat.group(1).equalsIgnoreCase(tokenType.toString()))
				{
					accessToken = mat.group(2);
				}
			}
			return accessToken;
		}
	}
}
