package osgi.component.configuration.command;

import java.io.IOException;

import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
	property = {
		"osgi.command.scope=test",
		"osgi.command.function=list"
	},
	service=ListCommand.class
)
public class ListCommand {
	
	ConfigurationAdmin cm;

    @Reference
    void setConfigurationAdmin(ConfigurationAdmin cm) {
        this.cm = cm;
    }
    
    public void list() {
    	try {
    		Configuration[] configurations = this.cm.listConfigurations(null);
    		
    		if (configurations != null) {
    			for (Configuration config : configurations) {
    				System.out.println(config.getPid() + " " + config.getFactoryPid());
    			}
    		}
    	} catch (InvalidSyntaxException | IOException exception) {
    		exception.printStackTrace();
    	}
    }
}