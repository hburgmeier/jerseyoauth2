package com.github.hburgmeier.jerseyoauth2.testsuite.rs2.guice;

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
import com.github.hburgmeier.jerseyoauth2.testsuite.rs2.services.CacheManagerProvider;
import com.github.hburgmeier.jerseyoauth2.testsuite.rs2.services.Configuration;
import com.github.hburgmeier.jerseyoauth2.testsuite.rs2.services.PersistenceProvider;
import com.github.hburgmeier.jerseyoauth2.testsuite.rs2.services.TestAuthorizationFlow;
import com.github.hburgmeier.jerseyoauth2.testsuite.rs2.ui.AllowServlet;
import com.google.inject.servlet.ServletModule;

public class AppModule  extends ServletModule {
	
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
    }
}
