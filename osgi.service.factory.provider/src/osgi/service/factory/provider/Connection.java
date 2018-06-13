package osgi.service.factory.provider;

import java.io.IOException;
import java.net.InetSocketAddress;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Map;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;

import com.uc4.communication.TimeoutException;
import com.uc4.communication.requests.XMLRequest;

import osgi.service.factory.interfaces.IConnection;

@Component(
	configurationPid="connection",
	configurationPolicy=ConfigurationPolicy.REQUIRE
)
public class Connection implements IConnection, ManagedService {
	private String name;
	
	private InetSocketAddress[] ip = new InetSocketAddress[5];
	
	private com.uc4.communication.Connection connection;
	
	private com.uc4.communication.requests.CreateSession session;
	
	@Activate
    void activate(Map<String, Object> properties) {
        this.name = (String) properties.get("name");
        
        
        for(int i=0; i<5; i++){
        	String temp = (String) properties.get(".ip." + i);
        	
        	if(!(temp == null)) {
            	String[] array = temp.split(":");
            	
            	try {
            		int port = Integer.parseInt(array[1]);
            		
            		InetSocketAddress cp = new InetSocketAddress(array[0], port);
            		
            		this.ip[i] = cp;
            	} catch (NumberFormatException e) {
            		e.printStackTrace();
            	}

//            	System.out.println(this.ip[i]);
        	}
        }
        
		System.out.println("connection " + this.name + " activated");
    }
	
	@Modified
    void modified(Map<String, Object> properties) {
		System.out.println("connection " + this.name + " modified");

		this.name = (String) properties.get("name");
    }

	@Override
	public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
		System.out.println("connection " + this.name + " updated");

		if(properties != null) {
			Enumeration<String> e = properties.keys();
			
	        while(e.hasMoreElements()) {
	            String k = e.nextElement();
	            System.out.println(k + ": " + properties.get(k));
	        }
		}
	}
	
	@Override
	public void open() {
		InetSocketAddress cp = this.ip[0];
		
		try {
			this.connection = com.uc4.communication.Connection.open(cp.getHostName(), cp.getPort());

			System.out.println("connection " + this.name + " opened");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		System.out.println("connection " + this.name + " closed");
		
		try {
			this.connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void login(int client, String user, String department, String password, char language) {
		try {
			this.session = this.connection.login(client, user, department, password, language);

			if (this.session.isLoginSuccessful()) {
				System.out.println(session.getWelcomeMessage());
				
				com.uc4.communication.ConnectionAttributes attributes = this.connection.getSessionInfo();
				System.out.println("connection sessionID: " + attributes.getSessionId());
			} else {
				if (this.session.getMessageBox() != null) {
					System.out.printf("%s %d %s\n",  this.session.getMessageBox().getType(), this.session.getMessageBox().getNumber(), this.session.getMessageBox().getText().toString().replace("\n", " "));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void send(XMLRequest request) {
		System.out.println("sending request " + request + " to connection " + this.connection);
		
		try {
			this.connection.sendRequestAndWait(request);
		} catch (TimeoutException | IOException e) {
			e.printStackTrace();
		}
	}
}