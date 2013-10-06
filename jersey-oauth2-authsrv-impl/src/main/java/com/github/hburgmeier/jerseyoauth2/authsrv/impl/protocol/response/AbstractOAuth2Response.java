package com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hburgmeier.jerseyoauth2.api.protocol.ResponseBuilderException;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.protocol.IHttpContext;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.protocol.IOAuth2Response;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.HttpHeaders;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.utils.UrlBuilder;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.utils.UrlBuilderException;

public abstract class AbstractOAuth2Response implements IOAuth2Response {

	protected final int statusCode;
	protected final ResponseFormat responseFormat;
	private Map<String, String> headerFields = new HashMap<>();

	public AbstractOAuth2Response(int statusCode, ResponseFormat responseFormat)
	{
		this.statusCode = statusCode;
		this.responseFormat = responseFormat;
		
		headerFields.put(HttpHeaders.CACHE_CONTROL,"no-store");
		headerFields.put(HttpHeaders.PRAGMA,"no-cache");
	}
	
	@Override
	public void render(IHttpContext context) throws ResponseBuilderException {
		context.setStatus(statusCode);
		if (responseFormat == ResponseFormat.JSON) {
			context.setContentType("application/json;charset=UTF-8");
		}
		
		for (Map.Entry<String, String> header : headerFields.entrySet())
		{
			context.setHeader(header.getKey(), header.getValue());
		}
	}
	
	protected void setHeader(String header, String value)
	{
		headerFields.put(header, value);
	}
	
	protected void renderJson(Map<String, Object> entity, IHttpContext context) throws ResponseBuilderException
	{
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(context.getResponseOutputStream(), entity);
		} catch (IOException e) {
			throw new ResponseBuilderException(e);
		}
	}
	
	protected void renderRedirect(Map<String, Object> entity, URI redirectUrl, IHttpContext context) throws ResponseBuilderException
	{
		try {
			UrlBuilder urlBuilder = new UrlBuilder();
			URI modifiedUrl = urlBuilder.addQueryParameters(entity, redirectUrl);

			context.setStatus(HttpServletResponse.SC_FOUND);
			context.setHeader(HttpHeaders.LOCATION, modifiedUrl.toString());
		} catch (UrlBuilderException e) {
			throw new ResponseBuilderException(e);
		}
	}
	
	protected void renderRedirectWithFragment(Map<String, Object> entity, URI redirectUrl, IHttpContext context) throws ResponseBuilderException
	{
		try {
			UrlBuilder urlBuilder = new UrlBuilder();
			String newFragment = urlBuilder.addQueryParameters(null, entity);
			URI modifiedUrl = urlBuilder.setFragment(redirectUrl, newFragment);

			context.setStatus(HttpServletResponse.SC_FOUND);
			context.setHeader(HttpHeaders.LOCATION, modifiedUrl.toString());
		} catch (UrlBuilderException e) {
			throw new ResponseBuilderException(e);
		}
	}	

}
