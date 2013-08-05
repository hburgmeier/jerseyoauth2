package com.github.hburgmeier.jerseyoauth2.rs.impl.guice;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceServletContextListener;

public abstract class BaseGuiceServletContextListener extends GuiceServletContextListener {

	@Override
	protected final Injector getInjector() {
		List<Module> modules = new LinkedList<>();
		modules.add(new ResourceServerModule());
		modules.addAll(getResourceServerModules());

		Injector injector = Guice.createInjector(modules);
		return injector;
	}

	protected abstract Collection<? extends Module> getResourceServerModules();

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
