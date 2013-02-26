package com.burgmeier.jerseyoauth2.resource;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;

import com.burgmeier.jerseyoauth.impl.test.OAuthGrizzlyTestContainerFactory;
import com.burgmeier.jerseyoauth2.impl.filter.OAuth20FilterFactory;
import com.burgmeier.jerseyoauth2.testsuite.guice.AppModule;
import com.google.inject.util.Modules;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;

@Deprecated
public class ClientTest extends JerseyTest {

	public ClientTest()
	{
		super(new OAuthGrizzlyTestContainerFactory(
				Modules.override(
						new AppModule()).with(new UnitTestModule())));
	}
	
	@Override
	protected AppDescriptor configure() {
		Map<String, String> params = new HashMap<String, String>();
		params.put(PackagesResourceConfig.PROPERTY_PACKAGES, "com.burgmeier.jerseyoauth.exampleone.resource");
		params.put(PackagesResourceConfig.FEATURE_DISABLE_WADL, "true");
		params.put(ResourceConfig.PROPERTY_RESOURCE_FILTER_FACTORIES, OAuth20FilterFactory.class.getName());
		
		WebAppDescriptor.Builder builder = new WebAppDescriptor.Builder(params);
		return builder.build();
	}	
	
//	@Test
	public void testInvalidResourceAccess()
	{
		WebResource resource = resource();
		ClientResponse result = resource.path("/sample/1").
				queryParam("access_token", "XXXXXXXXXX").
				get(ClientResponse.class);
		Assert.assertEquals(401, result.getStatus());
		
	}

}
