package osgi.automic.shell.core.commands;

// https://bitbucket.org/pjtr/net.luminis.cmc/overview

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.Descriptor;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
	property = {
		CommandProcessor.COMMAND_SCOPE + ":String=session",
		CommandProcessor.COMMAND_FUNCTION + ":String=create",
		CommandProcessor.COMMAND_FUNCTION + ":String=list",
		CommandProcessor.COMMAND_FUNCTION + ":String=table",
		CommandProcessor.COMMAND_FUNCTION + ":String=remove"
	},
	service=SessionCommands.class
)
public class SessionCommands {
	ConfigurationAdmin admin;

    @Reference
    void setConfigurationAdmin(ConfigurationAdmin admin) {
        this.admin = admin;
        
//        System.out.println("setting cm");
    }

    void unsetConfigurationAdmin(ConfigurationAdmin admin) {
        this.admin = null;
        
//        System.out.println("unsetting cm");
    }

    @Descriptor("create session")
    public void create(@Descriptor("user name") String user, String session) {
//    	System.out.println("creating session");
    	
    	try {
			Configuration config = this.admin.createFactoryConfiguration("session", "?");
			
			Hashtable<String, Object> properties = new Hashtable<>();
	        properties.put("user", user);
	        properties.put("ConnectionComponent.target", "(service.pid=" + session + ")");
	        
	        config.update(properties);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
    }

    @Descriptor("list sessions")
    public void list() throws IOException, InvalidSyntaxException {
		Configuration[] configurations = this.admin.listConfigurations("(service.factoryPid=session)");
		
		if (configurations != null && configurations.length > 0) {
			for (Configuration configuration : configurations) {
				System.out.println(configuration.getProperties().get("user") + " " + configuration.getProperties().get("service.pid"));
			}
		}
    }
    
    @Descriptor("list sessions")
    public void table() throws IOException, InvalidSyntaxException {
    	Configuration[] configurations = this.admin.listConfigurations("(service.factoryPid=session)");

    	if (configurations != null && configurations.length > 0) {
    		ShellTable table = new ShellTable();
    		table.header.add("user");
    		table.header.add("pid");

    		for (Configuration configuration : configurations) {
    			List<String> row = table.addRow();

    			//            row.add(info.selected ? "*" : "");
    			row.add((String) configuration.getProperties().get("user"));
    			row.add((String) configuration.getProperties().get("service.pid"));
    		}
        	
        	table.print();
    	}
    }
    
    @Descriptor("remove session")
	public void remove(String name) throws IOException, InvalidSyntaxException {
		Configuration[] configurations = this.admin.listConfigurations("(service.factoryPid=session)");
		
		for (Configuration configuration : configurations) {
			String conf_name = (String) configuration.getProperties().get("user");
			
			if (conf_name.equals(name)) {
				System.out.println("removing session " + configuration.getProperties().get("user"));
				
				configuration.delete();
			}
		}
	}
}