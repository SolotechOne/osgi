package osgi.event.provider.command;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.Descriptor;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import osgi.event.implementation.Messages;

import osgi.event.internal.IMessages;
import osgi.event.internal.MessageConstants;

@Component(
	property = {
		CommandProcessor.COMMAND_SCOPE + ":String=event",
		CommandProcessor.COMMAND_FUNCTION + ":String=messages",
		CommandProcessor.COMMAND_FUNCTION + ":String=kick"
	},
	service=Message.class
)
public class Message {
//	private IMessages messages;
	
//	@Activate
//    void activate(BundleContext context) {
////        System.out.println("MessagesCommand activated");
//        
////    	get single instance of Connection service
////    	System.out.println("------ single service reference --------");
//    	@SuppressWarnings("unchecked")
//		ServiceReference<Messages> sr = (ServiceReference<Messages>) context.getServiceReference("osgi.event.implementation.Messages");
////    	System.out.println(sr);
//    	
//    	this.messages = (Messages) context.getService(sr);
//    }
	
	@Reference
    Messages messages;

	@Descriptor("show messages")
	public void messages() throws IOException {
		this.messages.list();
    }
	
	@Reference
    EventAdmin eventAdmin;
	
	@Descriptor("kick event")
    public void kick(String kick, String target) {
		Event event = null;
		
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        String timestamp = sdfDate.format(now);
        
        Map<String, Object> properties = new HashMap<>();
        properties.put(MessageConstants.PROPERTY_KEY_MESSAGE_TEXT, timestamp + " aedev I0098273 Job 'JOBS.API@HELLO$WORLD' ended successfully.");
        properties.put(MessageConstants.PROPERTY_KEY_MESSAGE_TIMESTAMP, timestamp);
        properties.put(MessageConstants.PROPERTY_KEY_MESSAGE_SYSTEM, "aedev");
        properties.put(MessageConstants.PROPERTY_KEY_MESSAGE_TYPE, "I");
        properties.put(MessageConstants.PROPERTY_KEY_MESSAGE_NUMBER, "0098273");
        properties.put(MessageConstants.PROPERTY_KEY_MESSAGE_INSERTS, timestamp + "|I|0098273|JOBS.API@HELLO$WORLD");
                
		event = new Event(MessageConstants.TOPIC_MESSAGE, properties);
		
		if (event != null) {
            eventAdmin.postEvent(event);
        }
	}
}