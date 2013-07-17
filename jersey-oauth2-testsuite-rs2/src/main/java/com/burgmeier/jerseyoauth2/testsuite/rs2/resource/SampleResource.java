package com.burgmeier.jerseyoauth2.testsuite.rs2.resource;

import javax.jws.WebParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import com.burgmeier.jerseyoauth2.rs.api.IOAuthPrincipal;
import com.burgmeier.jerseyoauth2.rs.api.annotations.AllowedScopes;
import com.burgmeier.jerseyoauth2.rs.api.annotations.OAuth20;
import com.burgmeier.jerseyoauth2.testsuite.base.SampleEntity;

@Path("/sample")
public class SampleResource {

	@GET
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	@OAuth20
	@AllowedScopes(scopes={"test1", "test2"})
	public SampleEntity getEntity(@WebParam(name="id") String id, @Context SecurityContext securityContext)
	{
		IOAuthPrincipal principal = (IOAuthPrincipal)securityContext.getUserPrincipal();
		String username = principal.getUser().getName();
		String clientId = principal.getClientApp().getClientId();
		return new SampleEntity(id, username, clientId);
	}
	
}
