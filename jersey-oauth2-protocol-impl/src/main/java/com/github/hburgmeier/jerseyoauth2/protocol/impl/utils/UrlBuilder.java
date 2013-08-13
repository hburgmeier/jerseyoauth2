package com.github.hburgmeier.jerseyoauth2.protocol.impl.utils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class UrlBuilder {

	public URI addQueryParameters(Map<String, Object> parameters, URI baseUrl) throws UrlBuilderException
	{
		try {
			String newQuery = addQueryParameters(baseUrl.getQuery(), parameters);
			
			return new URI(baseUrl.getScheme(), baseUrl.getUserInfo(), baseUrl.getHost(), baseUrl.getPort(), baseUrl.getPath(), newQuery, baseUrl.getFragment());
		} catch (URISyntaxException e) {
			throw new UrlBuilderException(e);
		}
	}	
	
	public String addQueryParameters(String queryParams, Map<String, Object> newParameters) throws UrlBuilderException
	{
		try {
			Map<String, String> params;
			if (queryParams!=null)
				params = new LinkedHashMap<String, String>(parseQueryParameters(queryParams));
			else
				params = new LinkedHashMap<>();
			
			for (Map.Entry<String, Object> entry : newParameters.entrySet())
			{
				params.put(entry.getKey(), entry.getValue().toString());
			}
			
			return render(params);
		} catch (UnsupportedEncodingException e) {
			throw new UrlBuilderException(e);
		}
	}
	
	public URI setFragment(URI baseUri, String newFragment) throws UrlBuilderException
	{
		try {
			return new URI(baseUri.getScheme(), baseUri.getUserInfo(), baseUri.getHost(), 
					baseUri.getPort(), baseUri.getPath(), baseUri.getQuery(), newFragment);
		} catch (URISyntaxException e) {
			throw new UrlBuilderException(e);
		}
	}
	
	protected Map<String, String> parseQueryParameters(String query)
	{
		if (query == null)
			return null;
		
		Scanner scanner = new Scanner(query);
		try {
			scanner.useDelimiter("[&=]");
			
			Map<String, String> result = new LinkedHashMap<>();
			while (scanner.hasNext(".+"))
			{
				String key = scanner.next("[^=]+");
				String value = null;
				if (scanner.hasNext("[^&]+"))
				   value = scanner.next("[^&]+");
				result.put(key, value);
			}
			return result;
		} finally {
			scanner.close();
		}
	}
	
	protected String render(Map<String, String> params) throws UnsupportedEncodingException
	{
		StringBuffer buffer = new StringBuffer();
		for (Map.Entry<String, String> entry : params.entrySet())
		{
			if (buffer.length()>0)
				buffer.append("&");
			buffer.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.name()));
			buffer.append("=");
			if (entry.getValue()!=null)
				buffer.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.name()));
		}
		return buffer.toString();
	}
	
}
