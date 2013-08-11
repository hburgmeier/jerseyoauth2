package com.github.hburgmeier.jerseyoauth2.protocol.impl;

import javax.servlet.http.HttpServletRequest;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IHttpRequest;

public class HttpRequestAdapter implements IHttpRequest {

	protected final HttpServletRequest request;

	public HttpRequestAdapter(HttpServletRequest request) {
		super();
		this.request = request;
	}

	@Override
	public String getMethod() {
		return request.getMethod();
	}

	@Override
	public String getHeaderField(String field) {
		return request.getHeader(field);
	}

	@Override
	public String getQueryParameter(String queryParameter) {
		return request.getParameter(queryParameter);
	}

	@Override
	public String getFormParameterValue(String field) {
		return request.getParameter(field);
	}
	
	
}
