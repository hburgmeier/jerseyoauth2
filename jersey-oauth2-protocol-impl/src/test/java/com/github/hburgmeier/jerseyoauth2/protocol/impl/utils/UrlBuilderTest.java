package com.github.hburgmeier.jerseyoauth2.protocol.impl.utils;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

public class UrlBuilderTest {

	protected UrlBuilder urlBuilder = new UrlBuilder();
	
	@Test
	public void testParsing()
	{
		Map<String, String> params;
		
		params = urlBuilder.parseQueryParameters("a=b&c=d");
		assertEquals("b", params.get("a"));
		assertEquals("d", params.get("c"));
		
		params = urlBuilder.parseQueryParameters("a=b&c=");
		assertEquals("b", params.get("a"));
		assertEquals(null, params.get("c"));
	}
	
	@Test
	public void testRender() throws UrlBuilderException, URISyntaxException
	{
		URI baseUri = new URI("http://a@test1.bb.de/test/abc?abc=abc");
		Map<String, Object> parameters = new LinkedHashMap<>();
		parameters.put("cd","gh");
		parameters.put("numtest",1);
		URI result = urlBuilder.addQueryParameters(parameters, baseUri);
		assertEquals("http://a@test1.bb.de/test/abc?abc=abc&cd=gh&numtest=1", result.toString());
		
		parameters.put("abc","g");
		result = urlBuilder.addQueryParameters(parameters, baseUri);
		assertEquals("http://a@test1.bb.de/test/abc?abc=g&cd=gh&numtest=1", result.toString());
	}
}
