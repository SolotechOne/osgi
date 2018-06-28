package osgi.dependency.manager.temperature;

import org.apache.felix.dm.Component;
import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;

import org.osgi.framework.BundleContext;

import osgi.dependency.manager.sensor.interfaces.SensorService;
import osgi.dependency.manager.temperature.interfaces.TemperatureService;

public class Activator extends DependencyActivatorBase {

//	private static BundleContext context;
//
//	static BundleContext getContext() {
//		return context;
//	}

//	/*
//	 * (non-Javadoc)
//	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
//	 */
//	public void start(BundleContext bundleContext) throws Exception {
//		Activator.context = bundleContext;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
//	 */
//	public void stop(BundleContext bundleContext) throws Exception {
//		Activator.context = null;
//	}

	@Override
	public void init(BundleContext context, DependencyManager manager) throws Exception {
        Component component = manager.createComponent()
                .setInterface(TemperatureService.class.getName(), null)
                .setImplementation(TemperatureServiceImpl.class)
                .add(createServiceDependency().setService(SensorService.class).setRequired(true));

        manager.add(component);
    }

    @Override
    public void destroy(BundleContext bundleContext, DependencyManager dependencyManager) {
    }
}