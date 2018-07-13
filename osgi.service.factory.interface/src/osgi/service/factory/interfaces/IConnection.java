package osgi.service.factory.interfaces;

import com.uc4.communication.requests.XMLRequest;

public interface IConnection {
	public void open();
	
	public void close();
	
	public void login(int client, String user, String department, String password, char language);
	
	public void send(XMLRequest request);
	
	public void addNotificationListener();

	public void addKickEventListener();
}