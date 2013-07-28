package com.github.hburgmeier.jerseyoauth2.rs.impl.rs2.filter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IHttpRequest;

public class HttpRequestAdapter implements IHttpRequest {

	protected ContainerRequestContext context;
	protected Map<String,String> queryParameters = new HashMap<>();

	public HttpRequestAdapter(ContainerRequestContext ctx) {
		this.context = ctx;
		
		MultivaluedMap<String, String> queryParameters = ctx.getUriInfo().getQueryParameters();
		for (Entry<String, List<String>> entry : queryParameters.entrySet())
		{
			StringBuffer values = new StringBuffer();
			for (String val : entry.getValue())
			{
				if (values.length()>0)
					values.append(",");
				values.append(val);
			}
			this.queryParameters.put(entry.getKey(), values.toString());
		}
	}

	@Override
	public String getMethod() {
		return context.getMethod();
	}
	
	@Override
	public String getHeaderField(String field) {
		return context.getHeaderString(field);
	}
	
	@Override
	public String getQueryParameter(String queryParameter) {
		return queryParameters.get(queryParameter);
	}
}
