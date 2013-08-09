package com.github.hburgmeier.jerseyoauth2.protocol.impl.extractor;

import org.apache.commons.lang3.StringUtils;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IHttpRequest;

public class QueryParameterExtractor implements IExtractor {
	private String key;

	public QueryParameterExtractor(String key) {
		super();
		this.key = key;
	}

	@Override
	public String extractValue(IHttpRequest request) {
		return StringUtils.defaultIfEmpty(request.getQueryParameter(key), null);
	}
}
