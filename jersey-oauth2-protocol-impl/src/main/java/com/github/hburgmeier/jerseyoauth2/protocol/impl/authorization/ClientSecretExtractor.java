package com.github.hburgmeier.jerseyoauth2.protocol.impl.authorization;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.tuple.ImmutablePair;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IHttpRequest;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.HttpHeaders;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.extractor.CombinedExtractor;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.extractor.HeaderExtractor;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.extractor.IExtractor;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.oauth2.Constants;

public class ClientSecretExtractor implements IExtractor {

	private static final String BASIC_AUTH_PREFIX = "Basic ";
	private static final Pattern BASIC_AUTH_PWD_PATTERN = Pattern.compile("(.*):(.*)");
	
	protected CombinedExtractor secretExtractor = new CombinedExtractor(Constants.CLIENT_SECRET);
	protected HeaderExtractor authorizationExtractor = new HeaderExtractor(HttpHeaders.AUTHORIZATION);
	protected final boolean useAuthorizationHeader;
	
	public ClientSecretExtractor(boolean useAuthorizationHeader) {
		super();
		this.useAuthorizationHeader = useAuthorizationHeader;
	}

	@Override
	public String extractValue(IHttpRequest request) {
		String value = null;
		if (useAuthorizationHeader)
		{
			String authorization = authorizationExtractor.extractValue(request);
			if (authorization!=null && authorization.startsWith(BASIC_AUTH_PREFIX))
			{
				value = parseAuthorizationHeader(authorization).right;
			} else 
				value = secretExtractor.extractValue(request);
		} else
		   value = secretExtractor.extractValue(request);
		return value;
	}

	protected ImmutablePair<String, String> parseAuthorizationHeader(String auth)
	{
		try {
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
