package osgi.service.factory.commands;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.Descriptor;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.uc4.api.SearchResultItem;

import osgi.service.factory.interfaces.IConnection;
import osgi.service.factory.requests.SearchObject;

@Component(
	configurationPid="osgi.service.factory.commands.ConnectionCommands",
	configurationPolicy=ConfigurationPolicy.OPTIONAL,
	property= {
		CommandProcessor.COMMAND_SCOPE + "=jobs",
		CommandProcessor.COMMAND_FUNCTION + "=find",
		CommandProcessor.COMMAND_FUNCTION + "=edit"},
	service=JOBSCommands.class
)
public class JOBSCommands {
	@Reference(
		cardinality=ReferenceCardinality.AT_LEAST_ONE,
		policy=ReferencePolicy.DYNAMIC,
		target="(service.factoryPid=connection)"
	)
    private volatile List<IConnection> connections;
	
	@Descriptor("find automic job")
	public List<String> find(String filter) {
		SearchObject search = new SearchObject(filter);
		
		List<String> lines = new LinkedList<String>();
		
		for (ListIterator<IConnection> it = connections.listIterator(connections.size()); it.hasPrevious(); ) {
        	it.previous().send(search);
        	
//        	System.out.println(search.getName());
//        	System.out.println(search.isTypeJOBS());
        	
    		if (search.getMessageBox() != null) {
    			System.err.println(search.getMessageBox().getType() + " " + search.getMessageBox().getNumber() + " " + search.getMessageBox().getText().toString().replace("\n", " "));
    		}
    		
    		Iterator<SearchResultItem> result = search.resultIterator();
    		
    		if (result != null) {
    			while (result.hasNext()) {
    				SearchResultItem item = result.next();
    				
    				lines.add(item.getFolder() + " " + item.getName());
//    				System.out.println(item.getName() + " [" + item.getIcon() + "] (" + item.getFolder() + ")");
    			}
    		}
        }
		
		return lines;
	}
}