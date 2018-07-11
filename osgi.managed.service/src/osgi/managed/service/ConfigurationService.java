package osgi.managed.service;

import java.util.Dictionary;
import java.util.Enumeration;

import org.osgi.service.cm.ConfigurationEvent;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ConfigurationListener;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;

//@Component(
//		configurationPid="configurationservicefactory",
//		configurationPolicy=ConfigurationPolicy.REQUIRE
//	)
public class ConfigurationService implements IConfigurationService, ManagedService, ConfigurationListener {
	@interface ConfigurationServiceConfig {
        String system() default "";
    }
	
	private String system;
	
	@Activate
    void activate(ConfigurationServiceConfig config) {
        this.system = config.system();
        
        System.out.println("ConfigurationServiceConfig acticated: System = " + config.system());
    }
 
    @Modified
    void modified(ConfigurationServiceConfig config) {
        this.system = config.system();
        
        System.out.println("ConfigurationServiceConfig modified: System = " + config.system());
    }
	
	@Override
	public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
		if(properties != null) {
			if(properties.get("service.port").equals("")) {
				Throwable throwable = new Throwable("port is null");
//				throwable.fillInStackTrace();
				throw new ConfigurationException("fuck", "you", throwable);
//				System.out.println("port = " + properties.get("port"));
			}
						
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