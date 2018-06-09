package osgi.service.factory.provider;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Map;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;

import osgi.service.factory.interfaces.IConnection;

@Component(
	configurationPid="connection",
	configurationPolicy=ConfigurationPolicy.REQUIRE
)
public class Connection implements IConnection, ManagedService {
	private String name;
	
	@Activate
    void activate(Map<String, Object> properties) {
        this.name = (String) properties.get("name");
        
		System.out.println("connection " + this.name + " activated");
    }
	
	@Modified
    void modified(Map<String, Object> properties) {
		System.out.println("connection " + this.name + " modified");

		this.name = (String) properties.get("name");
    }
	
	@Override
	public void open() {
		System.out.println("connection " + this.name + " opened");
	}

	@Override
	public void close() {
		System.out.println("connection " + this.name + " closed");
	}

	@Override
	public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
		System.out.println("connection " + this.name + " updated");

		if(properties != null) {
			Enumeration<String> e = properties.keys();
			
	        while(e.hasMoreElements()) {
	            String k = e.nextElement();
	            System.out.println(k + ": " + properties.get(k));
	        }
		}
	}
}