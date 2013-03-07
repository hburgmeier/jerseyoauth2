package com.burgmeier.jerseyoauth2.client.scribe;

import java.io.IOException;
import java.util.Map;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.model.Token;

import com.fasterxml.jackson.databind.ObjectMapper;

public class OAuth20TokenExtractorImpl implements AccessTokenExtractor {

	@SuppressWarnings("rawtypes")
	@Override
	public Token extract(String response) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Map tokenMap = mapper.readValue(response, Map.class);
			if (!tokenMap.containsKey("access_token"))
				throw new TokenExtractorException(response);
			
			String accessToken = (String)tokenMap.get("access_token");
			String refreshToken = (String)tokenMap.get("refresh_token");
			String expiration = null;
			if (tokenMap.containsKey("expires_in"))
				expiration = ((Integer)tokenMap.get("expires_in")).toString();
			
			return new OAuth2Token(accessToken, refreshToken, expiration, response);
		} catch (IOException e) {
System.err.println("err:"+response);			
			throw new TokenExtractorException(response);
		}
	}

}
