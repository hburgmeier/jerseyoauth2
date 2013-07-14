package com.burgmeier.jerseyoauth2.testsuite.rs2.resource;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.burgmeier.jerseyoauth2.authsrv.api.client.ClientServiceException;
import com.burgmeier.jerseyoauth2.authsrv.api.client.ClientType;
import com.burgmeier.jerseyoauth2.authsrv.api.client.IClientService;
import com.burgmeier.jerseyoauth2.authsrv.api.client.IRegisteredClientApp;

@Path("/clients")
public class ClientsResource {

	private final IClientService clientService;
	
	public ClientsResource() //TODO
	{
		clientService = null;
	}
	
	@Inject
	public ClientsResource(final IClientService clientService) {
		super();
		this.clientService = clientService;
	}

	@POST
	@Produces({MediaType.APPLICATION_JSON})
	public ClientEntity createClient(@QueryParam("appName") String appName, @QueryParam("callbackUrl") String callbackUrl, @QueryParam("clientType") String clientTypeStr) throws ClientServiceException
	{
		ClientType clientType = "public".equalsIgnoreCase(clientTypeStr)?ClientType.PUBLIC:ClientType.CONFIDENTIAL;
		
		IRegisteredClientApp regClient = clientService.registerClient(appName, callbackUrl, clientType);
		return new ClientEntity(regClient);
	}
	
}

