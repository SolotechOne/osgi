package osgi.connection.implementation;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

import osgi.connection.interfaces.ISession;

@Component(
		service=Session.class,
		name="osgi.connection.implementation.Session"
		)
public class Session implements ISession {
	@SuppressWarnings("unused")
	private Connection connection;
	
	@Override
	public String getSystemName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getWelcomeMessage() {
		// TODO Auto-generated method stub
		return "Welcome message";
	}

	@Override
	public boolean isLoginSuccessful() {
		// TODO Auto-generated method stub
		return false;
	}

    @Activate
    void activate() {
        System.out.println("Session activated");
    }

    @Modified
    void modified() {
        System.out.println("Session modified");
    }

    @Deactivate
    void deactivate() {
        System.out.println("Session deactivated");
    }


	@Reference
	void setConnection(Connection connection) {
		this.connection = connection;
		System.out.println("Connection set");
	}

	void updateConnection(Connection connection) {
		this.connection = connection;
		System.out.println("Connection updated");
	}

	void unsetConnection(Connection connection) {
		this.connection = null;
		System.out.println("Connection unset");
	}
}