package com.github.hburgmeier.jerseyoauth2.authsrv.impl.endpoints.rest;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.hburgmeier.jerseyoauth2.api.protocol.ResponseBuilderException;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.protocol.IHttpContext;

public class RestHttpContext implements IHttpContext {
	
	protected ServletContext servletContext;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	
	public RestHttpContext(HttpServletRequest request,
			HttpServletResponse response, ServletContext servletContext) {
		super();
		this.request = request;
		this.response = response;
		this.servletContext = servletContext;
	}

	@Override
	public void forwardToPage(String relativeUrl) throws ResponseBuilderException {
		try {
			RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(relativeUrl);
			requestDispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			throw new ResponseBuilderException(e);
		}
	}

	@Override
	public void setStatus(int statusCode) {
		response.setStatus(statusCode);
	}

	@Override
	public void setContentType(String contextType) {
		response.setContentType(contextType);
	}

	@Override
	public void setHeader(String key, String value) {
		response.setHeader(key, value);
	}

	@Override
	public OutputStream getResponseOutputStream() throws ResponseBuilderException {
		try {
			return response.getOutputStream();
		} catch (IOException e) {
			throw new ResponseBuilderException(e);
		}
	}

	@Override
	public void addRequestAttribute(String key, Object value) {
		request.setAttribute(key, value);
	}
}
