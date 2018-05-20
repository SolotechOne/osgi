package osgi.connection.implementation;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceRegistration;

public class ConnectionFactory implements ServiceFactory<Connection>{
	private final String name;
	
	public ConnectionFactory(String name) {
		super();
		this.name = name;
	}

	@Override
	public Connection getService(Bundle bundle, ServiceRegistration<Connection> registration) {
		// TODO Auto-generated method stub
		return new Connection(this.name);
	}

	@Override
	public void ungetService(Bundle bundle, ServiceRegistration<Connection> registration, Connection service) {
		// TODO Auto-generated method stub
		
	}
}