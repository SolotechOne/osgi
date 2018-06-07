package osgi.managed.service;

import java.util.Dictionary;
import java.util.Enumeration;

import org.osgi.service.cm.ConfigurationEvent;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ConfigurationListener;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Modified;

public class ConfigurationService implements IConfigurationService, ManagedService, ConfigurationListener {
	@interface ConfigurationServiceConfig {
        String system() default "";
    }
	
	private String system;
	
	@Activate
    void activate(ConfigurationServiceConfig config) {
        this.system = config.system();
    }
 
    @Modified
    void modified(ConfigurationServiceConfig config) {
        this.system = config.system();
    }
	
	@Override
	public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
		if(properties != null) {
//			System.out.println("port = " + properties.get("port"));
			
			Enumeration<String> e = properties.keys();
			
	        while(e.hasMoreElements()) {
	            String k = e.nextElement();
	            System.out.println(k + ": " + properties.get(k));
	        }
		}
	}

	@Override
	public void configurationEvent(ConfigurationEvent event) {
		System.out.println("configuration Event "
				+ event.getType()
				+ " "
				+ (event.getFactoryPid() != null ? event.getFactoryPid() + "::"
						: "") + event.getPid());
	}

	@Override
	public int getData(int id) {
		System.out.println(this.system);
		return id;
	}
}