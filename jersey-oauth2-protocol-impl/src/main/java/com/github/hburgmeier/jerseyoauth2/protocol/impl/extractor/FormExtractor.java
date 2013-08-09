package com.github.hburgmeier.jerseyoauth2.protocol.impl.extractor;

import org.apache.commons.lang3.StringUtils;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IHttpRequest;

public class FormExtractor implements IExtractor {
	private String key;

	public FormExtractor(String key) {
		super();
		this.key = key;
	}

	@Override
	public String extractValue(IHttpRequest request) {
		return StringUtils.defaultIfEmpty(request.getFormParameterValue(key), null);
	}
}