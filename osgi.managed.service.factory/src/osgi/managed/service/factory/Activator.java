package osgi.managed.service.factory;

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
		System.out.println("init()");

		Properties props = new Properties();
		props.put(Constants.SERVICE_PID, ConfigurationServiceFactory.PID);

		manager.add(createComponent()
				.setInterface(ManagedServiceFactory.class.getName(), props)
				.setImplementation(ConfigurationServiceFactory.class));
		
//		manager.add(createComponent()
//				.setImplementation(ConfigurationService.class)
//				.add(createConfigurationDependency()
//						.setPid("configurationservice")
//						)
//				);
	}
}