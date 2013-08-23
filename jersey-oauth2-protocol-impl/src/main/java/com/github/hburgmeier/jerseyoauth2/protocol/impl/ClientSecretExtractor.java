package com.github.hburgmeier.jerseyoauth2.protocol.impl;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.EnumSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.tuple.ImmutablePair;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IHttpRequest;
import com.github.hburgmeier.jerseyoauth2.api.types.ParameterStyle;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.extractor.CombinedExtractor;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.extractor.HeaderExtractor;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.extractor.IExtractor;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.oauth2.Constants;

public class ClientSecretExtractor implements IExtractor {

	private static final String BASIC_AUTH_PREFIX = "Basic ";
	private static final Pattern BASIC_AUTH_PWD_PATTERN = Pattern.compile("(.*):(.*)");
	
	protected CombinedExtractor secretExtractor;
	protected HeaderExtractor authorizationExtractor = new HeaderExtractor(HttpHeaders.AUTHORIZATION);
	protected final boolean useAuthorizationHeader;
	protected boolean usedAuthorization; 
	
	public ClientSecretExtractor(boolean useAuthorizationHeader) {
		super();
		this.secretExtractor = new CombinedExtractor(Constants.CLIENT_SECRET);
		this.useAuthorizationHeader = useAuthorizationHeader;
	}
	
	public ClientSecretExtractor(boolean useAuthorizationHeader, EnumSet<ParameterStyle> parameterStyles) {
		super();
		this.secretExtractor = new CombinedExtractor(Constants.CLIENT_SECRET, parameterStyles);
		this.useAuthorizationHeader = useAuthorizationHeader;
	}

	@Override
	public String extractValue(IHttpRequest request) {
		String value = null;
		usedAuthorization = false;
		if (useAuthorizationHeader)
		{
			String authorization = authorizationExtractor.extractValue(request);
			if (authorization!=null && authorization.startsWith(BASIC_AUTH_PREFIX))
			{
				value = parseAuthorizationHeader(authorization).right;
				usedAuthorization = true;
			} else { 
				value = secretExtractor.extractValue(request);
			}
		} else {
		   value = secretExtractor.extractValue(request);
		}
		return value;
	}

	public boolean hasUsedAuthorization() {
		return usedAuthorization;
	}

	protected ImmutablePair<String, String> parseAuthorizationHeader(String auth)
	{
		try {
			String authStr = auth.substring(BASIC_AUTH_PREFIX.length());
			String decodedAuth = new String(Base64.decodeBase64(authStr),StandardCharsets.UTF_8.name());
			
			Matcher matcher = BASIC_AUTH_PWD_PATTERN.matcher(decodedAuth);
			if (matcher.matches())
			{
				return new ImmutablePair<String, String>(matcher.group(1), matcher.group(2));
			} else
				throw new IllegalArgumentException(auth);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}	
}
