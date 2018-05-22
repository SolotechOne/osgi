package osgi.connection.implementation;

import osgi.connection.interfaces.IConnection;

public class Connection implements IConnection {
	private String name;
	
	public Connection(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public void open(String servername, int port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void login(int client, String user, String department, String password, char language) {
		// TODO Auto-generated method stub
		
	}
}