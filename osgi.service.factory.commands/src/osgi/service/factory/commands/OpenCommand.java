package osgi.service.factory.commands;

import java.util.List;
import java.util.ListIterator;

import org.apache.felix.service.command.CommandProcessor;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import osgi.service.factory.interfaces.IConnection;

@Component(
	property= {
		CommandProcessor.COMMAND_SCOPE + "=ae",
		CommandProcessor.COMMAND_FUNCTION + "=open",
		CommandProcessor.COMMAND_FUNCTION + "=close"},
	service=OpenCommand.class
)
public class OpenCommand {
	@Reference(target="(service.factoryPid=connection)")
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
}