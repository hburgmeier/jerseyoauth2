package com.github.hburgmeier.jerseyoauth2.testsuite.guice;

import java.util.Arrays;
import java.util.Collection;

import com.github.hburgmeier.jerseyoauth2.authsrv.impl.guice.AuthorizationServerModule;
import com.github.hburgmeier.jerseyoauth2.rs.impl.guice.BaseGuiceServletContextListener;
import com.google.inject.Module;

public class ContextListener extends BaseGuiceServletContextListener {

	@Override
	protected Collection<? extends Module> getResourceServerModules() {
		return Arrays.asList(new AuthorizationServerModule(), new AppModule());
	}
	
}
