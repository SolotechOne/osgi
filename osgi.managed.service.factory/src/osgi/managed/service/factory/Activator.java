package osgi.managed.service.factory;

import java.util.Properties;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

import org.osgi.service.cm.ManagedServiceFactory;

import osgi.managed.service.ConfigurationService;

public class Activator extends DependencyActivatorBase {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	@Override
	public void init(BundleContext context, DependencyManager manager) throws Exception {
		System.out.println("init()");

//		Properties props = new Properties();
//		props.put(Constants.SERVICE_PID, ConfigurationServiceFactory.PID);

//		manager.add(createComponent()
//				.setInterface(ManagedServiceFactory.class.getName(), props)
//				.setImplementation(ConfigurationServiceFactory.class));
		
		Properties properties = new Properties();
		properties.put(Constants.SERVICE_PID, ConfigurationServiceFactory.PID);

//		manager.add(createComponent()
//				.setInterface(ManagedServiceFactory.class.getName(), properties)
//				.setImplementation(ConfigurationServiceFactory.class));
		
		
//		manager.add(createComponent()
//				.setImplementation(ConfigurationService.class)
//				.add(createConfigurationDependency()
//						.setPid("configurationservice")
//						)
//				);
		
		
//		manager.add(createComponent()
//		        .setInterface(new String[]{
//		                DeviceDriverManager.class.getName(),
//		                ManagedService.class.getName()},
//		            properties)
//		        .setImplementation(DriverManagerImpl.class)
//		        .add(createServiceDependency()
//		            .setService(DirectoryService.class)
//		            .setRequired(true))
//		        .add(createConfigurationDependency().setPid(PID)));
	}
}