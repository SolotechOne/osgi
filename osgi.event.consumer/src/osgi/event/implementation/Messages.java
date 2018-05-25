package osgi.event.implementation;

import java.util.LinkedList;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import osgi.event.internal.IMessages;

@Component(
	    property = {
	        "limit:Integer=2"
	    },
	    service=Messages.class
	)
public class Messages implements IMessages {
	private LinkedList<String> list = new LinkedList<String>();
	
	private int limit;
	
    @Activate
    void activate(Map<String, Object> properties) {
        this.limit = (Integer) properties.get("limit"); //$NON-NLS-1$
//        System.out.println(limit);
    }
    
	@Override
	public void add(String message) {
		list.addFirst(message);
		
		this.remove();
	}
	
	@Override
	public void remove() {
		if(list.size() > this.limit) {
			list.removeLast();
		}
	}

	@Override
	public void list() {
		for (String message : this.list) {
			System.out.println(message);
		}
	}
}