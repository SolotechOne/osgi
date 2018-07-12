package osgi.msg.service.provider.commands;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Iterator;
import java.util.Map;

import org.apache.felix.service.command.Descriptor;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import osgi.msg.service.interfaces.MessageEntry;
import osgi.msg.service.interfaces.MessageListener;
import osgi.msg.service.interfaces.MessageReaderService;
import osgi.msg.service.provider.listeners.MessageWriter;

@Component(
	property = {
		"osgi.command.scope=ae",
		"osgi.command.function=messages"},
	service=MessageCommand.class
)
public class MessageCommand {
	private MessageListener listener;

	private MessageReaderService messageReader;
	
	@Reference
	void addMessageReaderService(MessageReaderService service, Map<String, Object> properties) {
		this.messageReader=service;
	}

	void removeMessageReaderService(MessageReaderService service) {
		this.messageReader=null;
	}

	@Descriptor("show messages")
	public void messages() throws IOException {
		messages("list");
	}
	
	@Descriptor("show messages")
	public void messages(String command) throws IOException {
		switch(command) {
		case "on":
			if(this.listener==null) {
				this.listener=new MessageWriter();
			
				this.messageReader.addMessageListener(this.listener);
			}
			
			break;
		case "off":
			if(this.listener!=null) {
				this.messageReader.removeMessageListener(this.listener);
				
				this.listener=null;
			}
			
			break;
		default:
			Iterator<MessageEntry> iterator = this.messageReader.getLog();
			
			while(iterator.hasNext()) {
				MessageEntry entry = iterator.next();
				
				Instant instant = Instant.ofEpochMilli(entry.getTime());
				ZonedDateTime date = ZonedDateTime.ofInstant(instant, ZoneOffset.systemDefault());

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.YYYY HH:mm:ss");
				
//				System.out.println(formatter.format(date) + " [" + entry.getSystem() + "] " + entry.getType() + "-" + entry.getNumber() + " " + entry.getText() + " (" + entry.getInsert() + ")");

				System.out.printf("%s [%s] %s%07d %s (%s)\n", formatter.format(date), entry.getSystem(), entry.getType(), entry.getNumber(), entry.getText(), entry.getInsert());
			}
			
			break;
		}
    }
}