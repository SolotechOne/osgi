package osgi.service.factory.commands;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import org.apache.felix.service.command.CommandProcessor;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
		property= {
			CommandProcessor.COMMAND_SCOPE + "=ae",
			CommandProcessor.COMMAND_FUNCTION + "=scope"},
		service=ScopeCommand.class
	)
public class ScopeCommand {
	ConfigurationAdmin admin;

	@Reference
	void setConfigurationAdmin(ConfigurationAdmin admin) {
		this.admin = admin;
	}
	
	public void scope() throws IOException, InvalidSyntaxException {
//		for (ConfigurationService service : this.configurationServices) {
//			System.out.println(service.getData(id));
//		}
		
		String filter = "(service.factoryPid=connection)";
		
		Configuration[] configurations = this.admin.listConfigurations(filter);

		for (Configuration configuration : configurations) {
			System.out.println(
//				((active == configuration.getProperties().get("service.pid")) ? "*" : " ") + 
				configuration.getProperties().get("name") + " (" + 
				configuration.getProperties().get("service.pid") + ")"
			);
		}
	}
	
	public void scope(String name) throws IOException {
		Configuration config = this.admin.getConfiguration("osgi.service.factory.commands.OpenCommand");
		
		Dictionary<String, Object> properties = null;
//		Object target = null;
		
		if (config != null && config.getProperties() != null) {
			properties = config.getProperties();
//			target = properties.get("connections.target");
		} else {
			properties = new Hashtable<String, Object>();
		}

//		boolean isOnline = (target == null || target.toString().contains("online"));

		// toggle the state
//		StringBuilder filter = new StringBuilder("(service.connectivity=");
//		filter.append(isOnline ? "offline" : "online").append(")");
		
		String filter = "(&(service.factoryPid=connection)(name=" + name + "))";

		properties.put("connections.target", filter);
		
		config.update(properties);
	}
}