package osgi.event.implementation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import osgi.event.internal.IMessages;

@Component(
	    property = {
	        "limit:Integer=3"
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
		list.add(message);
		
		this.remove();
	}
	
	@Override
	public void remove() {
		if(list.size() > this.limit) {
			list.remove();
		}
	}

	@Override
	public void list() {
//		for (String message : this.list) {
//			System.out.println(message);
//		}
		
		Iterator<String> iterator = (Iterator<String>) list.iterator();
		
		list(iterator);
	}

	@Override
	public LinkedList<String> llist() {
		return list;
	}

	@Override
	public void reverse() {
//		int value= 1000;
//		int end = value>list.size()?list.size():value;
//		System.out.println(end);
		
//		((LinkedList<String>) list.subList(0, end)getClass()).descendingIterator();
		
		
//		List<String> sub = list.subList(0, lines);
//		Collections.reverse(sub);
		
		Iterator<String> iterator = (Iterator<String>) list.descendingIterator();
		
		list(iterator);
	}

	@Override
	public void clear() {
		list.clear();
	}

	@Override
	public int size() {
		return list.size();
	}
	
	private void list(Iterator<String> iterator) {
		while(iterator.hasNext()) {
			System.out.println(iterator.next());
		}
	}
}