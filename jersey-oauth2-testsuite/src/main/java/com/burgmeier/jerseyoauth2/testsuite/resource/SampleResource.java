package com.burgmeier.jerseyoauth2.testsuite.resource;

import javax.jws.WebParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.burgmeier.jerseyoauth2.api.annotations.AllowedScopes;
import com.burgmeier.jerseyoauth2.api.annotations.OAuth20;

@Path("/sample")
public class SampleResource {

	@GET
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	@OAuth20
	@AllowedScopes(scopes={"test1", "test2"})
	public SampleEntity getEntity(@WebParam(name="id") String id)
	{
		return new SampleEntity(id, "Test");
	}
	
}
