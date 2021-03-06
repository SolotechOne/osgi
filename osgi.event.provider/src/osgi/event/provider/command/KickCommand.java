package osgi.event.provider.command;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.felix.service.command.Descriptor;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import osgi.event.internal.MessageConstants;

//@Component(
//		property = {
//	        "osgi.command.scope=event",
//	        "osgi.command.function=kick" },
//	    service = KickCommand.class)
public class KickCommand {
	
	@Reference
    EventAdmin eventAdmin;
	
	@Activate
    void activate() {
//        System.out.println("KickCommand activated");
    }

    @Modified
    void modified() {
        System.out.println("KickCommand modified");
    }

    @Deactivate
    void deactivate() {
        System.out.println("KickCommand deactivated");
    }

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