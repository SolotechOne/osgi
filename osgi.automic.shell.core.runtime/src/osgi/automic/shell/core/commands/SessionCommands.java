package osgi.automic.shell.core.commands;

import java.io.IOException;
import java.util.Hashtable;

import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.Descriptor;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
	property = {
		CommandProcessor.COMMAND_SCOPE + ":String=session",
		CommandProcessor.COMMAND_FUNCTION + ":String=create"
	},
	service=SessionCommands.class
)
public class SessionCommands {
	ConfigurationAdmin admin;

    @Reference
    void setConfigurationAdmin(ConfigurationAdmin admin) {
        this.admin = admin;
    }
    
    @Descriptor("create session")
    public void create(@Descriptor("user name") String user) {
    	try {
			Configuration config = this.admin.createFactoryConfiguration("session");
			
			Hashtable<String, Object> properties = new Hashtable<>();
	        properties.put("user", user);
	        
	        config.update(properties);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
    }
}