package com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response;

import java.util.HashMap;
import java.util.Map;

import com.github.hburgmeier.jerseyoauth2.api.protocol.ResponseBuilderException;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.protocol.IHttpContext;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.protocol.IOAuth2Response;

public class ForwardOAuth2Response implements IOAuth2Response {

	private String relativeUrl;
	private Map<String, Object> requestAttributes = new HashMap<>();
	
	public ForwardOAuth2Response(String relativeUrl) {
		this.relativeUrl = relativeUrl;
	}
	
	public void addRequestAttribute(String key, Object value)
	{
		requestAttributes.put(key, value);
	}

	@Override
	public void render(IHttpContext context) throws ResponseBuilderException {
		context.forwardToPage(relativeUrl);
		
		for (Map.Entry<String, Object> entry : requestAttributes.entrySet())
		{
			context.addRequestAttribute(entry.getKey(), entry.getValue());
		}
	}
	
	
	
}
