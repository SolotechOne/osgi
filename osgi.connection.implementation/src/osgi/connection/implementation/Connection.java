package osgi.connection.implementation;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;

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

    @Activate
    void activate() {
        System.out.println("Connection activated");
    }

    @Modified
    void modified() {
        System.out.println("Connection modified");
    }

    @Deactivate
        void deactivate() {
        System.out.println("Connection deactivated");
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