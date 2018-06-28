package osgi.service.factory.provider;

import java.util.Properties;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

import org.osgi.service.cm.ManagedServiceFactory;

public class Activator extends DependencyActivatorBase {
	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}
	
	@Override
	public void init(BundleContext context, DependencyManager manager) throws Exception {
//		System.out.println(System.getProperty("felix.fileinstall.dir"));	// :C:\Users\Carsten\git\osgi\osgi.service.factory.provider\OSGI-INF

//		System.out.println(System.getProperty("felix.fileinstall.filter"));
		
//		Properties properties = new Properties();
//		properties.put(Constants.SERVICE_PID, ConnectionFactory.PID);
//
//		manager.add(createComponent()
//				.setInterface(ManagedServiceFactory.class.getName(), properties)
//				.setImplementation(ConnectionFactory.class));
	}
}