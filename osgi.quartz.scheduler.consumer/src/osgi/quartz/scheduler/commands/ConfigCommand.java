package osgi.quartz.scheduler.commands;

import java.io.IOException;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
	property = {
		"osgi.command.scope=test",
		"osgi.command.function=configure"
	},
	service=ConfigCommand.class
)
public class ConfigCommand {

    ConfigurationAdmin cm;

    @Reference
    void setConfigurationAdmin(ConfigurationAdmin cm) {
        this.cm = cm;
    }

    public void configure() throws IOException {
    	String threadCount = System.getProperty("org.quartz.threadPool.threadCount");
    	System.out.println(threadCount);
    	
    	try {
			for(Configuration config:cm.listConfigurations(null)) {
				System.out.println(config);	
			}
		} catch (InvalidSyntaxException e) {
			e.printStackTrace();
		}
    	
//        Configuration config = cm.getConfiguration("AdminConfiguredComponent");
//        
//        Hashtable<String, Object> props = new Hashtable<>();
//        props.put("message", msg);
//        props.put("iteration", count);
//        
//        config.update(props);
    }
}