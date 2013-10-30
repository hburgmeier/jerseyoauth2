package com.github.hburgmeier.jerseyoauth2.authsrv.impl.endpoints.servlet;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hburgmeier.jerseyoauth2.authsrv.api.IConfiguration;

@Singleton
public class StrictSecurityFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(StrictSecurityFilter.class);
	
	private final IConfiguration configuration;
	
	private boolean strictSecurityEnabled;
	
	@Inject
	public StrictSecurityFilter(IConfiguration configuration)
	{
		this.configuration = configuration;
	}
	
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)resp;
		if (strictSecurityEnabled && !request.isSecure())
		{
			LOGGER.error("Strict security switched on but insecure request received");
			response.sendError(HttpURLConnection.HTTP_BAD_REQUEST);
		} else {
			chain.doFilter(req, resp);
		}
		
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		this.strictSecurityEnabled = configuration.getStrictSecurity();
	}

}
