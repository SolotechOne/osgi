package osgi.component.configuration.external;

import java.io.IOException;
import java.util.Hashtable;

import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
    property = {
        "osgi.command.scope=test",
        "osgi.command.function=conf"
    },
    service=ConfCommand.class
)
public class ConfCommand {

    ConfigurationAdmin cm;

    @Reference
    void setConfigurationAdmin(ConfigurationAdmin cm) {
        this.cm = cm;
    }

    public void conf(String msg, int count) throws IOException {
//    	Use the double parameter getConfiguration(String, String) where location == null
//    	Only the component(s) of one bundle will receive the configuration object, as it will be bound to the bundle that first registers a service for the corresponding PID.    	
//        Configuration config = cm.getConfiguration("AdminConfiguredComponent");

//    	Use the single parameter getConfiguration(String)
//    	Calling the conf command on the console will result in nothing. As the configuration object is bound to the bundle of the command, the other bundles don’t see it and the contained components don’t get activated.
//        Configuration config = cm.getConfiguration("AdminConfiguredComponent", null);
    	
//    	Use the double parameter getConfiguration(String, String) where location == “?”
//    	The components of both bundles will receive the configuration object, as it is dispatched to all bundles that have visibility to the configuration. And as we didn’t mention and configure permissions, all our bundles receive it.
        Configuration config = cm.getConfiguration("AdminConfiguredComponent", "?");
        
        Hashtable<String, Object> props = new Hashtable<>();
        props.put("message", msg);
        props.put("iteration", count);
        
        config.update(props);
    }
}