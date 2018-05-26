package osgi.event.consumer;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

import osgi.event.implementation.Messages;

import osgi.event.internal.MessageConstants;

@Component(
	property = EventConstants.EVENT_TOPIC + "=" + MessageConstants.TOPIC_MESSAGE
)
public class MessageConsumer implements EventHandler {

//	private IMessages messages;
//	
//	@Activate
//    void activate(BundleContext context) {
////        System.out.println("MessageConsumer activated");
////        System.out.println(context);
//        
////    	get single instance of Connection service
////    	System.out.println("------ single service reference --------");
//    	@SuppressWarnings("unchecked")
//		ServiceReference<Messages> sr = (ServiceReference<Messages>) context.getServiceReference("osgi.event.implementation.Messages");
////    	System.out.println(sr);
//    	
//    	this.messages = (Messages) context.getService(sr);
////        
////        Framework framework=getFramework();
////        BundleContext bundleContext=framework.getBundleContext();
//    }
//
//    @Modified
//    void modified() {
//        System.out.println("MessageConsumer modified");
//    }
//
//    @Deactivate
//    void deactivate() {
//        System.out.println("MessageConsumer deactivated");
//    }

	@Reference
    Messages messages;

	@Override
	public void handleEvent(Event event) {
//		System.out.println("consumer: " + event.getProperty(MessageConstants.PROPERTY_KEY_MESSAGE_TEXT));
		
//		if(list.size() >= limit) {
//			list.removeLast();
//		}
		
//		list.addFirst((String) event.getProperty(MessageConstants.PROPERTY_KEY_MESSAGE_TEXT));
		
		this.messages.add((String) event.getProperty(MessageConstants.PROPERTY_KEY_MESSAGE_TEXT));
		
//		for (String message : list) {
//		    System.out.println(message);
//		}
	}
}