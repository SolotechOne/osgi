package osgi.msg.service.provider.listeners;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import osgi.msg.service.interfaces.MessageEntry;
import osgi.msg.service.interfaces.MessageListener;

public class MessageWriter implements MessageListener {
	@Override
	public void logged(MessageEntry entry) {
//		ZoneId cet = ZoneId.of("Europe/Paris");
		
		Instant instant = Instant.ofEpochMilli(entry.getTime());
		ZonedDateTime date = ZonedDateTime.ofInstant(instant, ZoneOffset.systemDefault());

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.YYYY HH:mm:ss");
		
//		System.out.println(formatter.format(date) + " [" + entry.getSystem() + "] " + entry.getType() + "-" + entry.getNumber() + " " + entry.getText() + " (" + entry.getInsert() + ")");

		System.out.printf("%s [%s] %s%07d %s (%s)\n", formatter.format(date), entry.getSystem(), entry.getType(), entry.getNumber(), entry.getText(), entry.getInsert());
	}
}