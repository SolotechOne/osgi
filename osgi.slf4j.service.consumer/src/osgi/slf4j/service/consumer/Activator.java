package osgi.slf4j.service.consumer;

import org.apache.log4j.Logger;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		Logger logger = Logger.getLogger(Activator.class);
		
	    logger.info("Hello World");
	}

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}
}