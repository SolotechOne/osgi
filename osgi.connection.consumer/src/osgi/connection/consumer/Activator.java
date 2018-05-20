package osgi.connection.consumer;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import osgi.connection.implementation.Connection;

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
		
		ServiceListener servicelistener = new ServiceListener() {
			@Override
			public void serviceChanged(ServiceEvent event) {
				System.out.println(event);
				
				ServiceReference<?> servicereference = event.getServiceReference();
				
				switch(event.getType()) {
				case ServiceEvent.REGISTERED:
					{
						Connection service = (Connection) context.getService(servicereference);

						System.out.println("ServiceListener: " + service.getName() + " registered");
					}
					break;
				case ServiceEvent.UNREGISTERING:
					{
						Connection service = (Connection) context.getService(servicereference);

						System.out.println("ServiceListener: " + service.getName() + " unregistered");
					}
					break;
				default:
					break;
				}
			}
		};

		String servicefilter = "(&(objectclass=osgi.connection.implementation.Connection)(service.scope=singleton))";
		bundleContext.addServiceListener(servicelistener, servicefilter);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
