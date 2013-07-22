package com.burgmeier.jerseyoauth2.authsrv.impl.amber;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.amber.oauth2.as.request.OAuthAuthzRequest;
import org.apache.amber.oauth2.common.OAuth;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.tuple.ImmutablePair;

public class OAuth2AuthzRequest extends OAuthAuthzRequest {

	private static final String BASIC_AUTH_PREFIX = "Basic ";
	private static final Pattern BASIC_AUTH_PWD_PATTERN = Pattern.compile("(.*):(.*)");
	private boolean enableAuthorizationHeader;
	
	public OAuth2AuthzRequest(HttpServletRequest request, boolean enableAuthorizationHeader) throws OAuthSystemException, OAuthProblemException {
		super(request);
		this.enableAuthorizationHeader = enableAuthorizationHeader;
	}

	@Override
	public String getClientId() {
		if (enableAuthorizationHeader && hasAuthorization())
		{
			return parseAuthorizationHeader().left;
		} else
			return super.getClientId();
	}

	@Override
	public String getClientSecret() {
		if (enableAuthorizationHeader && hasAuthorization())
		{
			return parseAuthorizationHeader().right;
		} else
			return super.getClientSecret();
	}

	protected boolean hasAuthorization() {
		String auth = request.getHeader(OAuth.HeaderType.AUTHORIZATION);
		return auth!=null && auth.startsWith(BASIC_AUTH_PREFIX);
	}
	
	protected ImmutablePair<String, String> parseAuthorizationHeader()
	{
		try {
			String auth = request.getHeader(OAuth.HeaderType.AUTHORIZATION);
			if (auth==null || !auth.startsWith(BASIC_AUTH_PREFIX))
				throw new IllegalArgumentException(auth);
			auth = auth.substring(BASIC_AUTH_PREFIX.length());
			
			String decodedAuth = new String(Base64.decodeBase64(auth),"utf-8");
			
			Matcher matcher = BASIC_AUTH_PWD_PATTERN.matcher(decodedAuth);
			if (matcher.matches())
			{
				ImmutablePair<String, String> pair = new ImmutablePair<String, String>(matcher.group(1), matcher.group(2));
				return pair;
			} else
				throw new IllegalArgumentException(auth);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
}
