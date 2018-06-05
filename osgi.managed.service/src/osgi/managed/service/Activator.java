package osgi.managed.service;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;

import org.osgi.service.cm.ManagedService;

public class Activator implements BundleActivator {

	private ServiceRegistration<?> serviceRegistration;
	
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
		
		ConfigurationService service = new ConfigurationService();
		
	    Dictionary<String,String> properties = new Hashtable<String,String>();
	    properties.put(Constants.SERVICE_PID, "configurationservice");
	    
	    String[] classes = new String[] {ConfigurationService.class.getName(), ManagedService.class.getName()};
	    
	    serviceRegistration = bundleContext.registerService(classes, service, properties);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
		
		serviceRegistration.unregister();
	}
}
