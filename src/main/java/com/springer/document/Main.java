package com.springer.document;

import javax.servlet.ServletException;

import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ListenerInfo;

/**
 * Application Starter (main function).
 */
public class Main {

	private Undertow server;

	public static void main(final String[] args) throws ServletException {
		new Main().start();
	}

	public void start() throws ServletException {
		DeploymentInfo servletBuilder =
			Servlets.deployment().setDeploymentName("wm.war").setClassLoader(Main.class.getClassLoader()).setContextPath("/wm")
				.addInitParameter("resteasy.guice.modules", MainGuiceModule.class.getName())
				.addListener(new ListenerInfo(GuiceResteasyBootstrapServletContextListener.class))
				.addServlets(Servlets.servlet("Resteasy", HttpServletDispatcher.class).addMapping("/*"));

		DeploymentManager manager = Servlets.defaultContainer().addDeployment(servletBuilder);
		manager.deploy();

		HttpHandler servletDeploymentHandler = manager.start();

		PathHandler pathHandler = Handlers.path(Handlers.redirect("/wm")).addPrefixPath("/wm", servletDeploymentHandler);

		server = Undertow.builder().addHttpListener(8080, "localhost").setHandler(pathHandler).build();

		server.start();
	}

	public void stop() {

		server.stop();

	}
}
