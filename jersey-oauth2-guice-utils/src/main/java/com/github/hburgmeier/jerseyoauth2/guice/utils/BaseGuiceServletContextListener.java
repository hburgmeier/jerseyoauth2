package com.github.hburgmeier.jerseyoauth2.guice.utils;

import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceServletContextListener;

public abstract class BaseGuiceServletContextListener extends GuiceServletContextListener {

	@Override
	protected final Injector getInjector() {
		Injector injector = Guice.createInjector(getApplicationModules());
		return injector;
	}

	protected abstract Collection<? extends Module> getApplicationModules();

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		// No call to super as it also calls getInjector()
		ServletContext sc = servletContextEvent.getServletContext();
		sc.setAttribute(Injector.class.getName(), getInjector());
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		ServletContext sc = servletContextEvent.getServletContext();
		sc.removeAttribute(Injector.class.getName());
		super.contextDestroyed(servletContextEvent);
	}
}
