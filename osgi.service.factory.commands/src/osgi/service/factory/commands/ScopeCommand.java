package osgi.service.factory.commands;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.CommandSession;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
	property= {
		CommandProcessor.COMMAND_SCOPE + "=ae",
		CommandProcessor.COMMAND_FUNCTION + "=scope"},
	service=ScopeCommand.class
)
public class ScopeCommand {
	private BundleContext context;
	
	@Activate
	public void activate(BundleContext context) {
		this.context = context;
	}
	
	ConfigurationAdmin admin;

	@Reference
	void setConfigurationAdmin(ConfigurationAdmin admin) {
		this.admin = admin;
	}
	
	private String active;
	
	public void scope() throws IOException, InvalidSyntaxException {
//		for (ConfigurationService service : this.configurationServices) {
//			System.out.println(service.getData(id));
//		}
		
		String filter = "(service.factoryPid=connection)";
		
		Configuration[] configurations = this.admin.listConfigurations(filter);
		
		for (Configuration configuration : configurations) {
			String service_name = (String) configuration.getProperties().get("name");
			String service_pid = (String) configuration.getProperties().get("service.pid");
			
//			System.out.println(active + " " + service_name);
			
			System.out.println(
				((service_name.equals(active)) ? "*" : " ") + 
				service_name + " (" + 
				service_pid + ")"
			);
		}
	}
	
	public void scope(CommandSession session, String name) throws IOException {
//		get all instances of Connection service with specified filter
		System.out.println("-------- all instances by filter -------");
		String filter = "(&(service.factoryPid=connection)(name=" + name + "))";
		ServiceReference[] connrefs;
		try {
			connrefs = context.getServiceReferences("osgi.service.factory.interfaces.IConnection", filter);
			
//			if (connrefs != null) {
//				for (ServiceReference ref : connrefs) {
//					System.out.println(ref);
//					Connection service = (Connection) context.getService(ref);
//				}
//			}
			
			if (connrefs.length == 1) {
				Configuration config = this.admin.getConfiguration("osgi.service.factory.commands.ConnectionCommands", "?");
				
				Dictionary<String, Object> properties = null;
//				Object target = null;
				
				if (config != null && config.getProperties() != null) {
					properties = config.getProperties();
//					target = properties.get("connections.target");
				} else {
					properties = new Hashtable<String, Object>();
				}
				
				this.active = name;
				
				session.put("prompt", this.active + "> ");

				//		boolean isOnline = (target == null || target.toString().contains("online"));
				
				// toggle the state
//				StringBuilder filter = new StringBuilder("(service.connectivity=");
//				filter.append(isOnline ? "offline" : "online").append(")");
				
//				String filter = "(&(service.factoryPid=connection)(name=" + name + "))";
//				String filter = "(&(service.factoryPid=connection))";
				
				properties.put("connections.target", filter);
				properties.put("connections.cardinality.minimum", 1);
				
				config.update(properties);
			}
		} catch (InvalidSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}