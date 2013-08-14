package com.github.hburgmeier.jerseyoauth2.sample.ui;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.hburgmeier.jerseyoauth2.api.user.IUser;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.ClientServiceException;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IClientService;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IRegisteredClientApp;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.user.IUserService;
import com.google.inject.Singleton;

@Singleton
public class AllowServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final IClientService clientService;
	private final IUserService userService;
	
	@Inject
	public AllowServlet(final IClientService clientService, final IUserService userService) {
		super();
		this.clientService = clientService;
		this.userService = userService;
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		IUser user = userService.getCurrentUser(request);
		
		String clientId = request.getParameter("client_id");
		IRegisteredClientApp clientApp = clientService.getRegisteredClient(clientId);
		
		String scopes = request.getParameter("scope");
		Set<String> allowedScopes = new HashSet<String>(Arrays.asList(scopes.split(" ")));
		
		try {
			clientService.authorizeClient(user, clientApp, allowedScopes);
			
			PrintWriter out = response.getWriter();
			out.println("<html><body><h1>Authorized</h1></body></html>");
		} catch (ClientServiceException e) {
			throw new ServletException(e);
		}
	}

}
