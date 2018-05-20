package conn.interfaces;

public interface IConnection {
	public void open(String servername, int port);
	
	public void close();
	
	public void login(int client, String user, String department, String password, char language);
}