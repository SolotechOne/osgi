package osgi.service.factory.provider;

import java.util.Dictionary;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;

public class ConnectionFactory implements ManagedServiceFactory {
	public static final String PID = "connection";

	@Override
	public String getName() {
		return "connection service factory";
	}

	@Override
	public void updated(String pid, Dictionary<String, ?> properties) throws ConfigurationException {
		System.out.println("updated factory " + pid + " -> " + properties);
	}

	@Override
	public void deleted(String pid) {
		System.out.println("deleting service " + pid);
	}
}