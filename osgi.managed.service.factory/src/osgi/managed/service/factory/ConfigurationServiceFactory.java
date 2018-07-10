package osgi.managed.service.factory;

import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.apache.felix.dm.Component;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;

import org.osgi.service.component.annotations.Reference;

import osgi.managed.service.ConfigurationService;

public class ConfigurationServiceFactory implements ManagedServiceFactory {
	public static final String PID = "configurationservicefactory";
	
	private volatile DependencyManager m_dependencyManager;
	
	private final Map<String, Component> m_components = new HashMap<String, Component>();
	
	ConfigurationAdmin cm;

    @Reference
    void setConfigurationAdmin(ConfigurationAdmin cm) {
        this.cm = cm;
    }
    
	@Override
	public String getName() {
		return "configuration service factory";
	}

	@Override
	public void updated(String pid, Dictionary<String, ?> properties) throws ConfigurationException {
		System.out.println("updated factory " + pid + " -> " + properties);
		
		if(properties != null) {
			String _propPort = properties.get("service.port").toString();
			
			try {
				int port = Integer.parseInt(_propPort);
				
				System.out.println("creating configuration service on port " + port);
				
				Component component = m_dependencyManager.createComponent()
						.setImplementation(ConfigurationService.class)
						.add(m_dependencyManager.createConfigurationDependency().setPid(pid));

				m_components.put(pid, component);
				
				m_dependencyManager.add(component);
			} catch (Exception exception) {
				throw new ConfigurationException("port", "port cannot be null", exception);
			}
			
//			if(properties.get("service.port").equals("")) {
//				Throwable throwable = new Throwable("port is null");
//				throwable.fillInStackTrace();
//				throw new ConfigurationException("fuck", "you", throwable);
//				System.out.println("port = " + properties.get("port"));
//			}
//			System.out.println("port = " + properties.get("port"));
		} else if (m_components.containsKey(pid)) {
			System.out.println(pid + " already exists");
			
			deleted(pid);
		}
		
//		if (m_components.containsKey(pid)) {
//			System.out.println(pid + " already exists");
			
//			m_dependencyManager.remove(m_components.remove(pid));
			
//			try {
//				for(Configuration conf : cm.listConfigurations(null)) {
//					System.out.println(conf);
//				}
//			} catch (IOException | InvalidSyntaxException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
			
			
//			Configuration c;
//			
//			try {
//				c = cm.getConfiguration(pid);
//
//				c.update(properties);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
//			Component comp = m_components.get(pid);
//			comp.setServiceProperties(properties);
			
//			return;
//		}

//		Component component = m_dependencyManager.createComponent()
//				.setImplementation(ConfigurationService.class)
//				.add(m_dependencyManager.createConfigurationDependency().setPid(pid));
//
//		m_components.put(pid, component);
//		m_dependencyManager.add(component);
	}

	@Override
	public void deleted(String pid) {
		System.out.println("deleting service " + pid);
		
		Component removed = m_components.remove(pid);
		
		if (removed != null) {
//			m_dependencyManager.remove(m_components.remove(pid));
		}
	}
}