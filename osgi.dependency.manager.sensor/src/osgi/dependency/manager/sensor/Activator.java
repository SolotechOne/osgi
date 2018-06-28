package osgi.dependency.manager.sensor;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import osgi.dependency.manager.sensor.interfaces.SensorService;

import java.util.Dictionary;
import java.util.Properties;

public class Activator implements BundleActivator {

    ServiceRegistration<SensorService> registration;

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public void start(BundleContext context) {
		Dictionary serviceProps = new Properties();
        serviceProps.put("type", "temperature");
        registration = context.registerService(SensorService.class, new SensorSimulator(), serviceProps);
    }

    @Override
    public void stop(BundleContext context) {
        // Not really necessary (if we don't unregister the service, the OSGi framework will do it for us), but let's make it explicit
        registration.unregister();
    }
}