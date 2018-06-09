package osgi.service.factory.commands;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.Descriptor;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
	property= {
		CommandProcessor.COMMAND_SCOPE + "=ae",
		CommandProcessor.COMMAND_FUNCTION + "=create"},
	service=CreateCommand.class
)
public class CreateCommand {
	ConfigurationAdmin configAdmin;
    
    @Reference
    void setConfigAdmin(ConfigurationAdmin admin) {
    	this.configAdmin = admin;
    }

    @Descriptor("creates a connection with given name")
    public void create(@Descriptor("name of connection") String name) {
    	create(name, null);
    }

    @Descriptor("creates a connection with given name")
    public void create(
    		@Descriptor("the name of the soldier to assimilate") String name,
    		@Descriptor("the new name for the assimilated Borg") String newName) {
        try {
            // filter to find the Borg created by the
            // Managed Service Factory with the given name
            String filter = "(&(name=" + name + ")" + "(service.factoryPid=connection))";
            Configuration[] configurations = this.configAdmin.listConfigurations(filter);

            if (configurations == null || configurations.length == 0) {
                //create a new configuration
                Configuration config = this.configAdmin.createFactoryConfiguration("connection", "?");
                Hashtable<String, Object> map = new Hashtable<>();
                if (newName == null) {
                    map.put("name", name);
                    System.out.println("connection created " + name);
                } else {
                    map.put("name", newName);
                    System.out.println("connection already exists " + name + " renamed to " + newName);
                }
                config.update(map);
            } else if (newName != null) {
                // update the existing configuration
                Configuration config = configurations[0];
                // it is guaranteed by listConfigurations() that only
                // Configuration objects are returned with non-null properties
                Dictionary<String, Object> map = config.getProperties();
                map.put("name", newName);
                config.update(map);
                System.out.println(name + " already exists and renamed to " + newName);
            }
        } catch (IOException | InvalidSyntaxException e1) {
            e1.printStackTrace();
        }
    }
}