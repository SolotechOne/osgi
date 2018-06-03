package osgi.connection.command;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.CommandSession;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.util.tracker.ServiceTracker;

import osgi.connection.implementation.Connection;
import osgi.connection.implementation.ConnectionFactory;

@Component(
	property = {
		CommandProcessor.COMMAND_SCOPE + ":String=conn",
		CommandProcessor.COMMAND_FUNCTION + ":String=open",
		CommandProcessor.COMMAND_FUNCTION + ":String=close",
		CommandProcessor.COMMAND_FUNCTION + ":String=scope",
		CommandProcessor.COMMAND_FUNCTION + ":String=switch",
		CommandProcessor.COMMAND_FUNCTION + ":String=workspace",
		CommandProcessor.COMMAND_FUNCTION + ":String=client"
	},
	service=ConnectionCommand.class
)
public class ConnectionCommand {
	private BundleContext context;
	
	private List<ServiceRegistration> serviceregistrations = new ArrayList<>();
	
	@Activate
	public void activate(BundleContext context) {
		this.context = context;
	}
	
	@Reference
	private Connection connection;
	
//	(
//			cardinality=ReferenceCardinality.MULTIPLE,
//			policy=ReferencePolicy.DYNAMIC,
//			target="(service.connectivity=online)"
//		)

//	@Reference
//	void addConnection(Connection connection, Map<String, Object> properties) {
//		this.connection = connection;
//	}
	
	public void open(String servername, int port) {
		Hashtable<String, String> properties = new Hashtable<String, String>();
		properties.put("description", "connection for system aeprd created by factory");
		properties.put("servername", servername);
		properties.put("port", String.valueOf(port));

		ConnectionFactory factory = new ConnectionFactory("aeprd");
		
		ServiceRegistration<?> registration = this.context.registerService(Connection.class.getName(), factory, properties);

		registration.setProperties(properties);
		
		serviceregistrations.add(registration);
	}

	public void close(int id) throws InvalidSyntaxException {
		String filter = "(&(objectclass=osgi.connection.implementation.Connection)(service.id=" + id + "))";
		
		ServiceReference[] servicereferences = context.getServiceReferences("osgi.connection.implementation.Connection", filter);
		
		if (servicereferences != null) {
			for (ServiceReference servicereference : servicereferences) {
				Connection connection = (Connection) context.getService(servicereference);

				for (Iterator<ServiceRegistration> iterator = this.serviceregistrations.iterator(); iterator.hasNext();) {
					ServiceRegistration registration = iterator.next();
					
					if(registration.getReference().equals(servicereference)) {
//						System.out.println(servicereference);
						
						registration.unregister();
						
				        iterator.remove();
				    }
				}
				
//				this.context.ungetService(servicereference);
			}
		}
		else {
			System.out.println("no connection service with id " + id + " found");
		}
	}
	
	public void scope() {
		ServiceTracker servicetracker = new ServiceTracker(context, Connection.class.getName(), null);
		servicetracker.open();

		ServiceReference[] servicereferences = servicetracker.getServiceReferences();
		
		if (servicereferences != null) {
			for (ServiceReference servicereference : servicereferences) {
				Connection connection = (Connection) context.getService(servicereference);

				System.out.println("name:" + connection.getName() + " " +
								   "id:" + servicereference.getProperty("service.id") + " " +
								   "host:" + servicereference.getProperty("servername") + " " +
								   "port:" + servicereference.getProperty("port"));
			}
		}

		servicetracker.close();
	}

	public void workspace(CommandSession session, int id) throws InvalidSyntaxException {
		String filter = "(&(objectclass=osgi.connection.implementation.Connection)(service.id=" + id + "))";
		
		ServiceReference[] servicereferences = context.getServiceReferences("osgi.connection.implementation.Connection", filter);
		
		if (servicereferences != null) {
			for (ServiceReference servicereference : servicereferences) {
				Connection connection = (Connection) context.getService(servicereference);
				
				this.connection = connection;
				
				session.put("prompt", connection.getName() + "> ");
			}
		}
		else {
			System.out.println("no connection service with id " + id + " found");
		}
		
		session.getConsole().flush();
	}
}