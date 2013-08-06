package com.github.hburgmeier.jerseyoauth2.testsuite.guice;

import java.util.Arrays;
import java.util.Collection;

import com.github.hburgmeier.jerseyoauth2.authsrv.impl.guice.AuthorizationServerModule;
import com.github.hburgmeier.jerseyoauth2.guice.utils.BaseGuiceServletContextListener;
import com.github.hburgmeier.jerseyoauth2.rs.impl.guice.ResourceServerModule;
import com.google.inject.Module;

public class ContextListener extends BaseGuiceServletContextListener {

	@Override
	protected Collection<? extends Module> getApplicationModules() {
		return Arrays.asList(
				new ResourceServerModule(),
				new AuthorizationServerModule(), 
				new AppModule());
	}
	
}
