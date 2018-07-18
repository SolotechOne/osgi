package osgi.component.configuration.command;

import java.io.IOException;
import java.util.Hashtable;

import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
	property = {
		"osgi.command.scope=test",
		"osgi.command.function=factory"
	},
	service=FactoryCommand.class
)
public class FactoryCommand {
	
	ConfigurationAdmin cm;

    @Reference
    void setConfigurationAdmin(ConfigurationAdmin cm) {
        this.cm = cm;
    }
    
    public void factory() {
    	try {
			Configuration config = this.cm.createFactoryConfiguration("AdminConfiguredComponent");
			
			Hashtable<String, Object> props = new Hashtable<>();
	        props.put("message", "fuck");
	        props.put("iteration", 2);
	        
	        config.update(props);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
    }
}