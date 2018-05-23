package osgi.event.consumer;

import osgi.event.internal.Constants;

import java.util.LinkedList;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

@Component(property = EventConstants.EVENT_TOPIC + "=" + Constants.TOPIC_MESSAGE)
public class MessageConsumer implements EventHandler {

	private LinkedList<String> list = new LinkedList<String>();
	private int limit = 5;
	
	@Override
	public void handleEvent(Event event) {
		System.out.println(event.getProperty(Constants.PROPERTY_KEY_MESSAGE_TEXT));
		
		if(list.size() >= limit) {
//			list.removeLast();
		}
		
		list.addFirst((String) event.getProperty(Constants.PROPERTY_KEY_MESSAGE_TEXT));
		
//		for (String message : list) {
//		    System.out.println(message);
//		}
	}
}