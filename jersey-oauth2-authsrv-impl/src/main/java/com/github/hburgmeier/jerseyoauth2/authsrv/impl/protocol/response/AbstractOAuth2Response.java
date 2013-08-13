package com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hburgmeier.jerseyoauth2.api.protocol.ResponseBuilderException;
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
	public void render(HttpServletResponse response) throws ResponseBuilderException {
		response.setStatus(statusCode);
		if (responseFormat == ResponseFormat.JSON)
			response.setContentType("application/json;charset=UTF-8");
		
		for (Map.Entry<String, String> header : headerFields.entrySet())
			response.setHeader(header.getKey(), header.getValue());
	}
	
	protected void setHeader(String header, String value)
	{
		headerFields.put(header, value);
	}
	
	protected void renderJson(Map<String, Object> entity, HttpServletResponse response) throws ResponseBuilderException
	{
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(response.getOutputStream(), entity);
		} catch (IOException e) {
			throw new ResponseBuilderException(e);
		}
	}
	
	protected void renderRedirect(Map<String, Object> tokenInfo, URI redirectUrl, HttpServletResponse response) throws ResponseBuilderException
	{
		try {
			UrlBuilder urlBuilder = new UrlBuilder();
			URI modifiedUrl = urlBuilder.addQueryParameters(tokenInfo, redirectUrl);

			response.setStatus(HttpServletResponse.SC_FOUND);
			response.setHeader(HttpHeaders.LOCATION, modifiedUrl.toString());
		} catch (UrlBuilderException e) {
			throw new ResponseBuilderException(e);
		}
	}
	
	protected void renderRedirectWithFragment(Map<String, Object> tokenInfo, URI redirectUrl, HttpServletResponse response) throws ResponseBuilderException
	{
		try {
			UrlBuilder urlBuilder = new UrlBuilder();
			String newFragment = urlBuilder.addQueryParameters(null, tokenInfo);
			URI modifiedUrl = urlBuilder.setFragment(redirectUrl, newFragment);

			response.setStatus(HttpServletResponse.SC_FOUND);
			response.setHeader(HttpHeaders.LOCATION, modifiedUrl.toString());
		} catch (UrlBuilderException e) {
			throw new ResponseBuilderException(e);
		}
	}	

}
