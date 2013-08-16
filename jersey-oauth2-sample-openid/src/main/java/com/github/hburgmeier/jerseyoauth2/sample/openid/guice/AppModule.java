package com.github.hburgmeier.jerseyoauth2.sample.openid.guice;

import java.util.HashMap;
import java.util.Map;

import com.github.hburgmeier.jerseyoauth2.authsrv.api.IConfiguration;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IClientIdGenerator;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IClientService;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.IAccessTokenStorageService;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.ITokenGenerator;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.ui.IAuthorizationFlow;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.user.IUserService;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.authorize.AuthorizationServlet;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.authorize.IssueAccessTokenServlet;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.services.DefaultPrincipalUserService;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.services.MD5TokenGenerator;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.services.UUIDClientIdGenerator;
import com.github.hburgmeier.jerseyoauth2.authsrv.openid.OpenIdServletFilter;
import com.github.hburgmeier.jerseyoauth2.sample.openid.Configuration;
import com.github.hburgmeier.jerseyoauth2.sample.openid.services.TestAccessTokenStorageService;
import com.github.hburgmeier.jerseyoauth2.sample.openid.services.TestAuthorizationFlow;
import com.github.hburgmeier.jerseyoauth2.sample.openid.services.TestClientService;
import com.github.hburgmeier.jerseyoauth2.sample.openid.ui.AllowServlet;
import com.google.inject.servlet.ServletModule;

public class AppModule extends ServletModule {

	@Override
	protected void configureServlets() {
    	bind(IUserService.class).to(DefaultPrincipalUserService.class);
    	bind(ITokenGenerator.class).to(MD5TokenGenerator.class);
    	bind(IClientIdGenerator.class).to(UUIDClientIdGenerator.class);
    	
    	bind(IConfiguration.class).to(Configuration.class);
    	
    	serve("/oauth2/auth").with(AuthorizationServlet.class);
    	serve("/oauth2/allow").with(AllowServlet.class);
    	serve("/oauth2/accessToken").with(IssueAccessTokenServlet.class);
    	
    	bind(IAccessTokenStorageService.class).to(TestAccessTokenStorageService.class);
    	bind(IClientService.class).to(TestClientService.class);
    	bind(IAuthorizationFlow.class).to(TestAuthorizationFlow.class);
    	
    	Map<String, String> filterParams = new HashMap<>();
    	filterParams.put("openid-service","https://www.google.com/accounts/o8/id");
		filter("/oauth2/auth", "/oauth2/allow").through(OpenIdServletFilter.class, filterParams);

	}

}
