package osgi.connection.implementation;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
//		Registering a very simple object as a service
		Connection aedev = new Connection("aedev");
//		aedev.connect("aedev");
		
		Hashtable<String, String> properties = new Hashtable<String, String>();
		properties.put("description", "connection to aedev");
		properties.put("servername", "s1597aeap001.koogrp.globus.net");
		properties.put("port", String.valueOf(2217));
		
		bundleContext.registerService(Connection.class.getName(), aedev, properties);
		
		
//		A simple service factory example
		Hashtable<String, String> factoryproperties = new Hashtable<String, String>();
		factoryproperties.put("description", "connection factory for system aeprd");
		factoryproperties.put("servername", "s1597aeap003.koogrp.globus.net");
		factoryproperties.put("port", String.valueOf(2220));

		ConnectionFactory factory = new ConnectionFactory("aeprd");
		bundleContext.registerService(Connection.class.getName(), factory, factoryproperties); 
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
