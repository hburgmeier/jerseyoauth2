package com.burgmeier.jerseyoauth2.testsuite.resource;

import java.util.Arrays;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.burgmeier.jerseyoauth2.api.client.ClientServiceException;
import com.burgmeier.jerseyoauth2.api.client.IAuthorizedClientApp;
import com.burgmeier.jerseyoauth2.api.client.IClientAuthorization;
import com.burgmeier.jerseyoauth2.api.client.IClientService;
import com.burgmeier.jerseyoauth2.api.client.IRegisteredClientApp;
import com.burgmeier.jerseyoauth2.api.user.IUser;
import com.burgmeier.jerseyoauth2.api.user.IUserService;
import com.google.inject.Inject;

@Path("/clientAuth")
public class ClientAuthResource {

	private final IClientService clientService;
	private final IUserService userService;
	
	@Inject
	public ClientAuthResource(IClientService clientService, final IUserService userService) {
		super();
		this.clientService = clientService;
		this.userService = userService;
	}

	@POST
	@Produces({MediaType.APPLICATION_JSON})
	public ClientAuthEntity authorize(@QueryParam("client_id") String clientId, 
			@QueryParam("scope") String scope, 
			@QueryParam("user_name") String userName,
			@Context HttpServletRequest request) throws ClientServiceException
	{
		IUser user = userService.getCurrentUser(request);
		
		IRegisteredClientApp clientApp = clientService.getRegisteredClient(clientId);
		String[] scopes = scope.split(" ");
		IAuthorizedClientApp authorizedClient = clientService.authorizeClient(user, clientApp, new HashSet<String>(Arrays.asList(scopes)));
		IClientAuthorization clientAuthorization = clientService.createClientAuthorization(authorizedClient);
		return new ClientAuthEntity(clientAuthorization);
	}
	
}
