package osgi.event.provider.command;

import java.util.HashMap;
import java.util.Map;

import org.apache.felix.service.command.Descriptor;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

@Component(
		property = {
	        "osgi.command.scope=event",
	        "osgi.command.function=kick" },
	    service = KickCommand.class)
public class KickCommand {
	
	@Reference
    EventAdmin eventAdmin;
	
	@Descriptor("kick event")
    public void kick(String kick, String target) {
		Event event = null;
		
		Map<String, Object> properties = new HashMap<>();
        properties.put(kick, target);
        
		event = new Event("kick", properties);
		
		if (event != null) {
            eventAdmin.postEvent(event);
        }
	}
}