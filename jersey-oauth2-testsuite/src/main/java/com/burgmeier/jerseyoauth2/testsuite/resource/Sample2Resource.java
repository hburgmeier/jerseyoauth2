package com.burgmeier.jerseyoauth2.testsuite.resource;

import javax.jws.WebParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import com.burgmeier.jerseyoauth2.api.IOAuthPrincipal;
import com.burgmeier.jerseyoauth2.api.annotations.AllowedScopes;
import com.burgmeier.jerseyoauth2.api.annotations.OAuth20;

@Path("/sample2")
@OAuth20
@AllowedScopes(scopes={"defaultScope"})
public class Sample2Resource {

	@GET
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public SampleEntity getEntity(@WebParam(name="id") String id, @Context SecurityContext securityContext)
	{
		IOAuthPrincipal principal = (IOAuthPrincipal)securityContext.getUserPrincipal();
		return new SampleEntity(id, principal.getUser().getName(), principal.getClientApp().getClientId());
	}
	
}
