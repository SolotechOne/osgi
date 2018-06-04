package osgi.managed.service.factory;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.apache.felix.dm.Component;
import org.apache.felix.dm.DependencyManager;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;

import osgi.managed.service.ConfigurationService;

public class ConfigurationServiceFactory implements ManagedServiceFactory {
	public static final String PID = "configurationservicefactory";
	 
	private volatile DependencyManager m_dependencyManager;
	
	private final Map<String, Component> m_components = new HashMap<String, Component>();
	
	@Override
	public String getName() {
		return "configuration service factory";
	}

	@Override
	public void updated(String pid, Dictionary<String, ?> properties) throws ConfigurationException {
		System.out.println("updated factory " + pid + " -> " + properties);
		
		if (m_components.containsKey(pid)) {
			return;
		}

		Component component = m_dependencyManager.createComponent()
				.setImplementation(ConfigurationService.class)
				.add(m_dependencyManager.createConfigurationDependency().setPid(pid));

		m_components.put(pid, component);
		m_dependencyManager.add(component);
	}

	@Override
	public void deleted(String pid) {
		System.out.println("deleting service " + pid);
		
		m_dependencyManager.remove(m_components.remove(pid));
	}
}