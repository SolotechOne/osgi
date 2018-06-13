package osgi.service.factory.interfaces;

public interface ISession {
	public String getSystemName();
	
	public String getWelcomeMessage();
	
	public boolean isLoginSuccessful();
}