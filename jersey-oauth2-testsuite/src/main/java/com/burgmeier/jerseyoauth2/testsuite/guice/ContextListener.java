package com.burgmeier.jerseyoauth2.testsuite.guice;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import com.burgmeier.jerseyoauth2.authsrv.impl.guice.AuthorizationServerModule;
import com.burgmeier.jerseyoauth2.rs.impl.guice.ResourceServerModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class ContextListener  extends GuiceServletContextListener {

	   @Override
	   protected Injector getInjector() {
	      Injector injector = Guice.createInjector(
	    		  new ResourceServerModule(),
	    		  new AuthorizationServerModule(),
	    		  new AppModule());
	      return injector;
	   }
	   
		@Override
		public void contextInitialized(ServletContextEvent servletContextEvent)
		{
			// No call to super as it also calls getInjector()
			ServletContext sc = servletContextEvent.getServletContext();
			sc.setAttribute(Injector.class.getName(), getInjector());
		}

		@Override
		public void contextDestroyed(ServletContextEvent servletContextEvent)
		{
			ServletContext sc = servletContextEvent.getServletContext();
			sc.removeAttribute(Injector.class.getName());
			super.contextDestroyed(servletContextEvent);
		}	   
}
