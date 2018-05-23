package osgi.connection.commands;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

import osgi.connection.implementation.Connection;

@Component(
	    property = {
	        "osgi.command.scope=automic",
	        "osgi.command.function=open"
	    },
	    service=OpenCommand.class
	)
public class OpenCommand {
	private Connection connection;
	
	public void open(String servername, int port) {
		System.out.println(connection.getName());
	}
	
    @Activate
    void activate() {
        System.out.println("open command: activated");
    }

    @Modified
    void modified() {
        System.out.println("open command: modified");
    }

    @Deactivate
    void deactivate() {
        System.out.println("open command: deactivated");
    }

	@Reference
	void setConnection(Connection connection) {
		System.out.println("open command: connection set");
		this.connection = connection;
	}
}