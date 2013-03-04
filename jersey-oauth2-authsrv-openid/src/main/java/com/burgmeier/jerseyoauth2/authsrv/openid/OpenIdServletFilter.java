package com.burgmeier.jerseyoauth2.authsrv.openid;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class OpenIdServletFilter implements Filter {

	private OpenIdConsumer consumer;
	private String openidServiceId;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		consumer = new OpenIdConsumer(filterConfig.getServletContext());
		openidServiceId = filterConfig.getInitParameter(OpenIdConstants.PARAM_OPENID_SERVICE);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest hrequest = (HttpServletRequest)request;
		HttpSession session = hrequest.getSession();
		
		if (session.getAttribute(OpenIdConstants.OPENID_SESSION_VAR)!=null)
		{
			
		} else {
			if (session.getAttribute(OpenIdConstants.OPENID_DISC)!=null)
			{
				consumer.verifyResponse(hrequest);
			} else
				consumer.authRequest(openidServiceId, getRequestUrl(hrequest), hrequest, (HttpServletResponse)response);
		}
		
		
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		consumer = null;
	}

	private String getRequestUrl(HttpServletRequest request) {
		return request.getRequestURL().toString();
	}	
}
