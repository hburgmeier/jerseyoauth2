package com.github.hburgmeier.jerseyoauth2.protocol.impl.extractor;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IHttpRequest;

public class QueryParameterExtractor implements IExtractor {
	private String key;

	public QueryParameterExtractor(String key) {
		super();
		this.key = key;
	}

	@Override
	public String extractValue(IHttpRequest request) {
		return request.getQueryParameter(key);
	}
}
