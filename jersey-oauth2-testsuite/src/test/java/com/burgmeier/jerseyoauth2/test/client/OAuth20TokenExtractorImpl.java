package com.burgmeier.jerseyoauth2.test.client;

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
System.err.println(response);			
			return new Token((String)tokenMap.get("access_token"), "", response);
		} catch (IOException e) {
			throw new IllegalArgumentException(response);
		}
	}

}
