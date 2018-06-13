package osgi.service.factory.commands;

import java.util.List;
import java.util.ListIterator;

import org.apache.felix.service.command.CommandProcessor;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import osgi.service.factory.interfaces.IConnection;

@Component(
	configurationPid="osgi.service.factory.commands.ConnectionCommands",
	property= {
		CommandProcessor.COMMAND_SCOPE + "=ae",
		CommandProcessor.COMMAND_FUNCTION + "=open",
		CommandProcessor.COMMAND_FUNCTION + "=close",
		CommandProcessor.COMMAND_FUNCTION + "=login"},
	service=OpenCommand.class
)
public class OpenCommand {
	@Reference(
		cardinality=ReferenceCardinality.AT_LEAST_ONE,
		policy=ReferencePolicy.DYNAMIC,
//	    policyOption=ReferencePolicyOption.GREEDY,
		target="(service.factoryPid=connection)"
	)
    private volatile List<IConnection> connections;
 
    public void open() {
        for (ListIterator<IConnection> it = connections.listIterator(connections.size()); it.hasPrevious(); ) {
        	it.previous().open();
        }
    }

    public void close() {
        for (ListIterator<IConnection> it = connections.listIterator(connections.size()); it.hasPrevious(); ) {
        	it.previous().close();
        }
    }

    public void login() {
    	login(49, "API", "API", "IZNMGOE00EYC3VZA7G8085W6BJ4UFKYR", 'D');
    }
    
    public void login(int client, String user, String department, String password, char language) {
        for (ListIterator<IConnection> it = connections.listIterator(connections.size()); it.hasPrevious(); ) {
        	it.previous().login(client, user, department, password, language);
        }
    }
}