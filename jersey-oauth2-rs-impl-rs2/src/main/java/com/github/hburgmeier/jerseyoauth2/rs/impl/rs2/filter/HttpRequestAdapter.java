package com.github.hburgmeier.jerseyoauth2.rs.impl.rs2.filter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.io.IOUtils;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IHttpRequest;
import com.github.hburgmeier.jerseyoauth2.rs.impl.rs2.filter.util.FormParser;
import com.github.hburgmeier.jerseyoauth2.rs.impl.rs2.filter.util.FormParserException;

public class HttpRequestAdapter implements IHttpRequest {

	protected ContainerRequestContext context;
	protected Map<String,String> queryParameters = new HashMap<>();
	protected Form form;

	public HttpRequestAdapter(ContainerRequestContext ctx) {
		this.context = ctx;
		
		MultivaluedMap<String, String> queryParams = ctx.getUriInfo().getQueryParameters();
		for (Entry<String, List<String>> entry : queryParams.entrySet())
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
	
	@Override
	public String getFormParameterValue(String formParameter) {
		if (form==null)
			form = getForm();
		return form.asMap().getFirst(formParameter);
	}
	
	protected Form getForm()
	{
		if (MediaType.APPLICATION_FORM_URLENCODED_TYPE.equals(context.getMediaType()) && context.hasEntity())
		{
            try {
				InputStream inputStream = context.getEntityStream();
				if (inputStream.getClass() != ByteArrayInputStream.class) {
				    // Buffer input
				    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				    try {
				        IOUtils.copy(inputStream, byteArrayOutputStream);
				    } catch (IOException e) {
				        throw new IllegalArgumentException(e);
				    }

				    inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
				    context.setEntityStream(inputStream);
				}

				ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) inputStream;
				FormParser formParser = new FormParser();
				Form f = formParser.parseForm(byteArrayInputStream);
				byteArrayInputStream.reset();
				return f;
			} catch (FormParserException e) {
				return new Form();
			}
        } else {
            return new Form();
        }		
	}

}
