package com.github.hburgmeier.jerseyoauth2.rs.impl.filter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.MultivaluedMap;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IHttpRequest;
import com.sun.jersey.api.representation.Form;
import com.sun.jersey.spi.container.ContainerRequest;

public class HttpRequestAdapter implements IHttpRequest {

	protected ContainerRequest containerRequest;
	protected Map<String,String> queryParameters = new HashMap<>();
	protected Form form;

	public HttpRequestAdapter(ContainerRequest containerRequest) {
		this.containerRequest = containerRequest;
		
		MultivaluedMap<String, String> queryParameters = containerRequest.getQueryParameters();
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
		return containerRequest.getMethod();
	}
	
	@Override
	public String getHeaderField(String field) {
		return containerRequest.getHeaderValue(field);
	}
	
	@Override
	public String getQueryParameter(String queryParameter) {
		return queryParameters.get(queryParameter);
	}
	
	@Override
	public String getFormParameterValue(String field) {
		if (form==null)
			form = containerRequest.getFormParameters();
		
		return form.getFirst(field);
	}
}
