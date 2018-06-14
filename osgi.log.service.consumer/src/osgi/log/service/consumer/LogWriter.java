package osgi.log.service.consumer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import osgi.log.service.interfaces.LogEntry;
import osgi.log.service.interfaces.LogListener;
import osgi.log.service.interfaces.LogService;

public class LogWriter implements LogListener {
	@Override
	public void logged(LogEntry entry) {
		ZoneId cet = ZoneId.of("Europe/Paris");
		
		Instant instant = Instant.ofEpochMilli(entry.getTime());
		ZonedDateTime date = ZonedDateTime.ofInstant(instant, ZoneOffset.systemDefault());
//		ZonedDateTime date = ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);
//		ZonedDateTime date = ZonedDateTime.ofInstant(instant, cet);

//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss:SSS");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.YYYY HH:mm:ss");
//		String output = formatter.format ( zdt );

//		System.out.println ( "millisecondsSinceEpoch: " + millisecondsSinceEpoch + " instant: " + instant + " output: " + output );
		
		
		
//		Date date = new Date(entry.getTime());
//		DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
//		DateFormat formatter = new SimpleDateFormat("dd.MM.YYYY HH:mm:ss");
//		String dateFormatted = formatter.format(date);
		
		
		
		switch (entry.getLevel()) {
			case LogService.LOG_DEBUG: {
				System.out.println(formatter.format(date) + " [" + entry.getBundle().getSymbolicName() + "] DEBUG(" + entry.getLevel() + ") " + entry.getMessage());
				break;
			}
			case LogService.LOG_ERROR: {
				System.out.println(formatter.format(date) + " [" + entry.getBundle().getSymbolicName() + "] ERROR(" + entry.getLevel() + ") " + entry.getMessage());
				break;
			}
			case LogService.LOG_INFO: {
				System.out.println(formatter.format(date) + " [" + entry.getBundle().getSymbolicName() + "] INFO(" + entry.getLevel() + ") " + entry.getMessage());
				break;
			}
			case LogService.LOG_WARNING: {
				System.out.println(formatter.format(date) + " [" + entry.getBundle().getSymbolicName() +  "] WARNING(" + entry.getLevel() + ") " + entry.getMessage());
				break;
			}
		}
	}
}