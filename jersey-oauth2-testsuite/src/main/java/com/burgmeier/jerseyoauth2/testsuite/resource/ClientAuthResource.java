package com.burgmeier.jerseyoauth2.testsuite.resource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;

import com.burgmeier.jerseyoauth2.api.client.IAuthorizedClientApp;
import com.burgmeier.jerseyoauth2.api.user.IUser;
import com.burgmeier.jerseyoauth2.authsrv.api.IConfiguration;
import com.burgmeier.jerseyoauth2.authsrv.api.client.ClientServiceException;
import com.burgmeier.jerseyoauth2.authsrv.api.client.IClientAuthorization;
import com.burgmeier.jerseyoauth2.authsrv.api.client.IClientService;
import com.burgmeier.jerseyoauth2.authsrv.api.client.IRegisteredClientApp;
import com.burgmeier.jerseyoauth2.authsrv.api.user.IUserService;
import com.google.inject.Inject;

@Path("/clientAuth")
public class ClientAuthResource {

	private final IClientService clientService;
	private final IUserService userService;
	private final IConfiguration configuration;
	
	@Inject
	public ClientAuthResource(IClientService clientService, final IUserService userService, final IConfiguration configuration) {
		super();
		this.clientService = clientService;
		this.userService = userService;
		this.configuration = configuration;
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
		Set<String> authScopes = configuration.getDefaultScopes();
		if (scope!=null)
		{
			String[] scopes = scope.split(" ");
			if (!(scopes.length==1 && StringUtils.isEmpty(scopes[0])))
				authScopes = new HashSet<String>(Arrays.asList(scopes));
		}
		IAuthorizedClientApp authorizedClient = clientService.authorizeClient(user, clientApp, authScopes);
		IClientAuthorization clientAuthorization = clientService.createClientAuthorization(authorizedClient);
		return new ClientAuthEntity(clientAuthorization);
	}
	
}
