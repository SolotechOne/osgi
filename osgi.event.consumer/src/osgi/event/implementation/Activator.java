package osgi.event.implementation;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		Activator.context = context;
		
//		Registering a very simple object as a service
		Messages messages = new Messages();
		
		Hashtable<String, String> properties = new Hashtable<String, String>();
		properties.put("description", "messages");
		
		context.registerService(Messages.class.getName(), messages, properties);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
	}
}