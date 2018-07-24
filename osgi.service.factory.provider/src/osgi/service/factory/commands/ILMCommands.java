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

import com.uc4.api.systemoverview.ILMPartitionItem;
import com.uc4.communication.requests.ILMList;

import osgi.service.factory.interfaces.IConnection;

@Component(
	configurationPid="osgi.service.factory.commands.ConnectionCommands",
	configurationPolicy=ConfigurationPolicy.OPTIONAL,
	property= {
		CommandProcessor.COMMAND_SCOPE + "=ilm",
		CommandProcessor.COMMAND_FUNCTION + "=list",
		CommandProcessor.COMMAND_FUNCTION + "=table"},
	service=ILMCommands.class
)
public class ILMCommands {
	@Reference(
		cardinality=ReferenceCardinality.AT_LEAST_ONE,
		policy=ReferencePolicy.DYNAMIC,
		target="(service.factoryPid=connection)"
	)
    private volatile List<IConnection> connections;
	
	@Descriptor("list ilm partitions")
	public List<String> list() {
		ILMList ilm = new ILMList();
		
		List<String> lines = new LinkedList<String>();
		
		for (ListIterator<IConnection> it = connections.listIterator(connections.size()); it.hasPrevious(); ) {
        	it.previous().send(ilm);
        	
//        	System.out.println(search.getName());
//        	System.out.println(search.isTypeJOBS());
        	
    		if (ilm.getMessageBox() != null) {
    			System.err.println(ilm.getMessageBox().getType() + " " + ilm.getMessageBox().getNumber() + " " + ilm.getMessageBox().getText().toString().replace("\n", " "));
    		}
    		
			System.out.println(ilm.getStatusText() + " " + ilm.getOnlinePartitions());
			
    		Iterator<ILMPartitionItem> result = ilm.partitions();
    		
    		if (result != null) {
    			while (result.hasNext()) {
    				ILMPartitionItem item = result.next();
    				
    				lines.add(item.getNumber() + " (" + item.getParent() + ") " + item.getFileGroupTableSpace() + " " + item.getStart() + " " + item.getMinRunID() + " " + item.getMaxRunID() + " ");
//    				System.out.println(item.getName() + " [" + item.getIcon() + "] (" + item.getFolder() + ")");
    			}
    		}
        }
		
		return lines;
	}

	@Descriptor("list ilm partitions as table")
	public List<String> table() {
		ILMList ilm = new ILMList();
		
		List<String> lines = new LinkedList<String>();
		
		for (ListIterator<IConnection> it = connections.listIterator(); it.hasNext(); ) {
        	it.next().send(ilm);
        	
//        	System.out.println(search.getName());
//        	System.out.println(search.isTypeJOBS());
        	
    		if (ilm.getMessageBox() != null) {
    			System.err.println(ilm.getMessageBox().getType() + " " + ilm.getMessageBox().getNumber() + " " + ilm.getMessageBox().getText().toString().replace("\n", " "));
    		}
    		
			System.out.println(ilm.getStatusText() + " " + ilm.getOnlinePartitions());
			
    		Iterator<ILMPartitionItem> result = ilm.partitions();
    		
    		if (result != null) {
    			ShellTable table = new ShellTable();
        		table.header.add("partition");
        		table.header.add("parent");
        		table.header.add("tablespace");
        		table.header.add("starttime");
        		table.header.add("runid min");
        		table.header.add("runid max");

        		while (result.hasNext()) {
    				ILMPartitionItem item = result.next();
    				
    				List<String> row = table.addRow();
    				
    				row.add(String.valueOf(item.getNumber()));
    				row.add(item.getParent());
    				row.add(item.getFileGroupTableSpace());
    				row.add(item.getStart().toString());
    				row.add(String.valueOf(item.getMinRunID()));
    				row.add(String.valueOf(item.getMaxRunID()));
    				
//    				lines.add(item.getNumber() + " (" + item.getParent() + ") " + item.getFileGroupTableSpace() + " " + item.getStart() + " " + item.getMinRunID() + " " + item.getMaxRunID() + " ");
//    				System.out.println(item.getName() + " [" + item.getIcon() + "] (" + item.getFolder() + ")");
    			}
        		
        		table.print();
    		}
        }
		
		return lines;
	}
}