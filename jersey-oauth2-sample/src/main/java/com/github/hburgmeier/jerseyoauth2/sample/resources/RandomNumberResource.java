package com.github.hburgmeier.jerseyoauth2.sample.resources;

import java.util.Random;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.github.hburgmeier.jerseyoauth2.rs.api.annotations.AllowedScopes;
import com.github.hburgmeier.jerseyoauth2.rs.api.annotations.OAuth20;
import com.github.hburgmeier.jerseyoauth2.sample.services.ScopeConstants;

@Path("/random")
@OAuth20
@AllowedScopes(scopes={ScopeConstants.RANDOM})
public class RandomNumberResource {

	private Random randomGen = new Random(System.currentTimeMillis());
	
	@GET
	@Produces({MediaType.TEXT_PLAIN})
	public String getRandomNumber()
	{
		return Integer.toString(randomGen.nextInt());
	}
	
}
