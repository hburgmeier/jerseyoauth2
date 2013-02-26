package com.burgmeier.jerseyoauth2.testsuite.guice;

import java.util.HashMap;
import java.util.Map;

import com.burgmeier.jerseyoauth2.api.IConfiguration;
import com.burgmeier.jerseyoauth2.api.client.IClientService;
import com.burgmeier.jerseyoauth2.api.token.IAccessTokenService;
import com.burgmeier.jerseyoauth2.api.ui.IAuthorizationFlow;
import com.burgmeier.jerseyoauth2.api.user.IUserService;
import com.burgmeier.jerseyoauth2.impl.authorize.AuthorizationServlet;
import com.burgmeier.jerseyoauth2.impl.authorize.IssueAccessTokenServlet;
import com.burgmeier.jerseyoauth2.impl.filter.OAuth20FilterFactory;
import com.burgmeier.jerseyoauth2.testsuite.resource.AccessTokenServiceProvider;
import com.burgmeier.jerseyoauth2.testsuite.services.Configuration;
import com.burgmeier.jerseyoauth2.testsuite.services.TestAccessTokenService;
import com.burgmeier.jerseyoauth2.testsuite.services.TestAuthorizationFlow;
import com.burgmeier.jerseyoauth2.testsuite.services.TestClientService;
import com.burgmeier.jerseyoauth2.testsuite.services.TestUserService;
import com.burgmeier.jerseyoauth2.testsuite.ui.AllowServlet;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class AppModule  extends JerseyServletModule {
	
    @Override
    protected void configureServlets() {
    	bind(AccessTokenServiceProvider.class);
    	
    	bind(IAccessTokenService.class).to(TestAccessTokenService.class);
    	bind(IClientService.class).to(TestClientService.class);
    	bind(IUserService.class).to(TestUserService.class);
    	bind(IConfiguration.class).to(Configuration.class);
    	bind(IAuthorizationFlow.class).to(TestAuthorizationFlow.class);
    	
    	serve("/oauth2/auth").with(AuthorizationServlet.class);
    	serve("/oauth2/allow").with(AllowServlet.class);
    	serve("/oauth2/accessToken").with(IssueAccessTokenServlet.class);
    	
       Map<String, String> params = new HashMap<String, String>();
       params.put(PackagesResourceConfig.PROPERTY_PACKAGES, "com.burgmeier.jerseyoauth2.testsuite.resource");
//see http://java.net/jira/browse/JERSEY-630	            
       params.put(PackagesResourceConfig.FEATURE_DISABLE_WADL, "true");
       params.put(ResourceConfig.PROPERTY_RESOURCE_FILTER_FACTORIES, OAuth20FilterFactory.class.getName());
       // Route all requests for rest resources through GuiceContainer
       serve("/rest/*").with(GuiceContainer.class, params);
    }
}
