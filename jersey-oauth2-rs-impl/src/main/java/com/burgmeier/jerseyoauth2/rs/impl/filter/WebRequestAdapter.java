package com.burgmeier.jerseyoauth2.rs.impl.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sun.jersey.spi.container.ContainerRequest;

class WebRequestAdapter implements HttpServletRequest {

	private final ContainerRequest containerRequest;
	private String queryString;
	
	public WebRequestAdapter(ContainerRequest containerRequest) {
		this.containerRequest = containerRequest;
		StringBuffer queryStr = new StringBuffer();
		for (Map.Entry<String, List<String>> entry : this.containerRequest.getQueryParameters().entrySet())
		{
			queryStr.append(entry.getKey()).append("=");
			queryStr.append(entry.getValue().get(0)).append("&"); //TODO multivalue
			
		}
		this.queryString = queryStr.toString();
	}

	@Override
	public Object getAttribute(String arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Enumeration<?> getAttributeNames() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getCharacterEncoding() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getContentLength() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getContentType() {
		if (containerRequest.getMediaType()!=null)
			return containerRequest.getMediaType().toString();
		else
			return null;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getLocalAddr() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getLocalName() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getLocalPort() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Locale getLocale() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Enumeration<?> getLocales() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getParameter(String arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Map<?,?> getParameterMap() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Enumeration<?> getParameterNames() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String[] getParameterValues(String arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getProtocol() {
		throw new UnsupportedOperationException();
	}

	@Override
	public BufferedReader getReader() throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getRealPath(String arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getRemoteAddr() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getRemoteHost() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getRemotePort() {
		throw new UnsupportedOperationException();
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getScheme() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getServerName() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getServerPort() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSecure() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeAttribute(String arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setAttribute(String arg0, Object arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCharacterEncoding(String arg0)
			throws UnsupportedEncodingException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getAuthType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getContextPath() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Cookie[] getCookies() {
		throw new UnsupportedOperationException();
	}

	@Override
	public long getDateHeader(String arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getHeader(String arg0) {
		return containerRequest.getHeaderValue(arg0);
	}

	@Override
	public Enumeration<?> getHeaderNames() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Enumeration<?> getHeaders(String arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getIntHeader(String arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getMethod() {
		return containerRequest.getMethod();
	}

	@Override
	public String getPathInfo() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getPathTranslated() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getQueryString() {
		return queryString;
	}

	@Override
	public String getRemoteUser() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getRequestURI() {
		throw new UnsupportedOperationException();
	}

	@Override
	public StringBuffer getRequestURL() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getRequestedSessionId() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getServletPath() {
		throw new UnsupportedOperationException();
	}

	@Override
	public HttpSession getSession() {
		throw new UnsupportedOperationException();
	}

	@Override
	public HttpSession getSession(boolean arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Principal getUserPrincipal() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isRequestedSessionIdFromUrl() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isUserInRole(String arg0) {
		throw new UnsupportedOperationException();
	}

}
