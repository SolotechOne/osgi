package osgi.connection.consumer;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

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
		
//	get single instance of Connection service
	System.out.println("------ single service reference --------");
	ServiceReference<Connection> sr = (ServiceReference<Connection>) bundleContext.getServiceReference("conn.implementation.Connection");
	System.out.println(sr);
	
//	get all instances of Connection service
	System.out.println("------- all instances of service -------");
	ServiceReference[] refs = bundleContext.getServiceReferences(osgi.connection.implementation.Connection.class.getName(), null);
	if (refs != null) {
		for (ServiceReference ref : refs) {
			System.out.println(ref);
			Connection service = (Connection) bundleContext.getService(ref);
		}
	}
	
//	get all instances of Connection service with specified filter
	System.out.println("-------- all instances by filter -------");
	String filter = "(&(objectclass=osgi.connection.implementation.Connection)(service.id=62))";
	ServiceReference[] connrefs = bundleContext.getServiceReferences("osgi.connection.implementation.Connection", filter);
	if (connrefs != null) {
		for (ServiceReference ref : connrefs) {
			System.out.println(ref);
			Connection service = (Connection) bundleContext.getService(ref);
		}
	}
	
//	track all instances of Connection service
	System.out.println("---- track all instances of service ----");
	ServiceTracker servicetracker = new ServiceTracker(bundleContext, Connection.class.getName(), null);
	servicetracker.open();
	
	
	ServiceReference[] trackrefs = servicetracker.getServiceReferences();
	if (trackrefs != null) {
		for (ServiceReference ref : trackrefs) {
			System.out.println("ServiceTracker: " + ref);
			Connection service = (Connection) bundleContext.getService(ref);
		}
	}
			
			
	Connection conn = (Connection) servicetracker.getService();
	System.out.println("ServiceTracker: " + conn.getName());
	
	servicetracker.close();


////	track all instances of Session service
//	System.out.println("---- track all instances of session ----");
//	ServiceTracker sessiontracker = new ServiceTracker(bundleContext, Session.class.getName(), null);
//	sessiontracker.open();
//	
//	
//	ServiceReference[] tracksess = sessiontracker.getServiceReferences();
//	if (tracksess != null) {
//		for (ServiceReference ref : tracksess) {
//			System.out.println("ServiceTracker: " + ref);
//			Session service = (Session) bundleContext.getService(ref);
//			
//			System.out.println(service);
//		}
//	}
//			
//			
//	Session session = (Session) sessiontracker.getService();
//	System.out.println("ServiceTracker: " + session.getWelcomeMessage());
//	
//	sessiontracker.close();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
