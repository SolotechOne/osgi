package osgi.log.service.consumer;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.osgi.service.component.annotations.Component;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

@Component(
	property = EventConstants.EVENT_TOPIC + "=osgi/msg/service/provider/MessageEntry/*"
)
public class MessageEventHandler implements EventHandler {
	@Override
	public void handleEvent(Event event) {
		Instant instant = Instant.ofEpochMilli((long) event.getProperty("time"));
		ZonedDateTime date = ZonedDateTime.ofInstant(instant, ZoneOffset.systemDefault());

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.YYYY HH:mm:ss");
		
//		System.out.println(formatter.format(date) + " [" + event.getProperty("system") + "] " + event.getProperty("type") + "-" + event.getProperty("number") + " " + event.getProperty("text") + " (" + event.getProperty("insert") + ")");

//		System.out.printf("%s [%s] %s%07d %s (%s)\n", formatter.format(date), event.getProperty("system"), event.getProperty("type"), event.getProperty("number"), event.getProperty("text"), event.getProperty("insert"));
	}
}