package osgi.managed.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(
		property= {
			"osgi.command.scope:String=connection",
			"osgi.command.function:String=scope",
			"osgi.command.function:String=connect"},
		service=ConnectionScope.class
	)
public class ConnectionScope {
	@Reference
    ConfigurationAdmin configAdmin;
	
//	private List<ConfigurationService> configurationServices = new ArrayList<>();
//
//	@Reference(
//		cardinality=ReferenceCardinality.MULTIPLE,
//		policy=ReferencePolicy.DYNAMIC
////		,target="(service.id=76)"
//	)
//	void addConfigurationService(ConfigurationService service, Map<String, Object> properties) {
//		this.configurationServices.add(service);
//
//		System.out.println("Added " + service.getClass().getName());
//		properties.forEach((k, v) -> {
//			System.out.println(k+"="+v);
//		});
//		
//		System.out.println();
//	}
//
//	void removeConfigurationService(ConfigurationService service) {
//		this.configurationServices.remove(service);
//		
//		System.out.println("Removed " + service.getClass().getName());
//	}

	private String active;
	
	public void scope() throws IOException, InvalidSyntaxException {
//		for (ConfigurationService service : this.configurationServices) {
//			System.out.println(service.getData(id));
//		}
		
		String filter = "(&(service.factoryPid=configurationservicefactory))";
		
		Configuration[] configurations = this.configAdmin.listConfigurations(filter);

		for (Configuration configuration : configurations) {
			System.out.println(
				((active == configuration.getProperties().get("service.pid")) ? "*" : " ") + 
				configuration.getProperties().get("system") + " (" + 
				configuration.getProperties().get("description") + ")"
			);
		}
	}
	
	public void scope(String system) throws IOException, InvalidSyntaxException {
		String filter = "(&(system=" + system + ")" + "(service.factoryPid=configurationservicefactory))";

		Configuration[] configurations = this.configAdmin.listConfigurations(filter);
		
		for (Configuration configuration : configurations) {
			this.active = (String) configuration.getProperties().get("service.pid");
		}
		
		
		Configuration config = this.configAdmin.getConfiguration("osgi.managed.service.ConnectionScope");
		
		Dictionary<String, Object> props = null;
		Object target = null;
		
		if (config != null && config.getProperties() != null) {
			props = config.getProperties();
			target = props.get("ConfigurationService.target");
			
			System.out.println(target);
		} else {
			props = new Hashtable<String, Object>();
		}
		
		for (Enumeration e = props.elements(); e.hasMoreElements();) {
	         System.out.println(e.nextElement());
	      }
	}
	
	
	private List<ConfigurationService> configurationServices = new ArrayList<>();

	@Reference(
		cardinality=ReferenceCardinality.MULTIPLE,
		policy=ReferencePolicy.DYNAMIC,
		target="(service.pid=configurationservicefactory)"
	)
	void addConfigurationService(ConfigurationService service, Map<String, Object> properties) {
		this.configurationServices.add(service);

		System.out.println("Added " + service.getClass().getName());
		
		properties.forEach((k, v) -> {
			System.out.println(k+"="+v);
		});
		
		System.out.println();
	}

	void removeConfigurationService(ConfigurationService service) {
		this.configurationServices.remove(service);
		
		System.out.println("Removed " + service.getClass().getName());
	}
	
	public void connect() {
		for (ConfigurationService service : this.configurationServices) {
			System.out.println(service.getData(1));
		}
	}
}