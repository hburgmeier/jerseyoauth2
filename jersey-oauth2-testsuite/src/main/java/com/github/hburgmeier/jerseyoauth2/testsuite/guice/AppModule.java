package com.github.hburgmeier.jerseyoauth2.testsuite.guice;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import net.sf.ehcache.CacheManager;

import com.github.hburgmeier.jerseyoauth2.authsrv.api.IConfiguration;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IClientService;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.IAccessTokenStorageService;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.ITokenGenerator;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.ui.IAuthorizationFlow;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.user.IUserService;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.authorize.AuthorizationServlet;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.authorize.IssueAccessTokenServlet;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.services.DefaultPrincipalUserService;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.services.MD5TokenGenerator;
import com.github.hburgmeier.jerseyoauth2.authsrv.jpa.CachingAccessTokenStorage;
import com.github.hburgmeier.jerseyoauth2.authsrv.jpa.DatabaseClientService;
import com.github.hburgmeier.jerseyoauth2.rs.api.IRSConfiguration;
import com.github.hburgmeier.jerseyoauth2.rs.impl.filter.OAuth20FilterFactory;
import com.github.hburgmeier.jerseyoauth2.testsuite.base.ui.AllowServlet;
import com.github.hburgmeier.jerseyoauth2.testsuite.services.CacheManagerProvider;
import com.github.hburgmeier.jerseyoauth2.testsuite.services.Configuration;
import com.github.hburgmeier.jerseyoauth2.testsuite.services.PersistenceProvider;
import com.github.hburgmeier.jerseyoauth2.testsuite.services.TestAuthorizationFlow;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class AppModule  extends JerseyServletModule {
	
    @Override
    protected void configureServlets() {
    	bind(IAccessTokenStorageService.class).to(CachingAccessTokenStorage.class);
    	bind(IClientService.class).to(DatabaseClientService.class);
    	bind(IConfiguration.class).to(Configuration.class);
    	bind(IRSConfiguration.class).to(Configuration.class);
    	bind(IAuthorizationFlow.class).to(TestAuthorizationFlow.class);
    	
    	bind(IUserService.class).to(DefaultPrincipalUserService.class);
    	bind(ITokenGenerator.class).to(MD5TokenGenerator.class);
    	
    	bind(EntityManagerFactory.class).toProvider(new PersistenceProvider());
    	bind(CacheManager.class).toProvider(new CacheManagerProvider());
    	
    	serve("/oauth2/auth").with(AuthorizationServlet.class);
    	serve("/oauth2/allow").with(AllowServlet.class);
    	serve("/oauth2/accessToken").with(IssueAccessTokenServlet.class);
    	
       Map<String, String> params = new HashMap<String, String>();
       params.put(PackagesResourceConfig.PROPERTY_PACKAGES, "com.github.hburgmeier.jerseyoauth2.testsuite.resource;" +
       		"com.github.hburgmeier.jerseyoauth2.testsuite.base.resource");
//see http://java.net/jira/browse/JERSEY-630	            
       params.put(PackagesResourceConfig.FEATURE_DISABLE_WADL, "true");
       params.put(ResourceConfig.PROPERTY_RESOURCE_FILTER_FACTORIES, OAuth20FilterFactory.class.getName());
// Route all requests for rest resources through GuiceContainer
       serve("/rest/*").with(GuiceContainer.class, params);
    }
}
