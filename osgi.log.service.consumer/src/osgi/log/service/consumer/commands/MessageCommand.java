package osgi.log.service.consumer.commands;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.apache.felix.service.command.Descriptor;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import osgi.log.service.consumer.MessageWriter;

import osgi.msg.service.interfaces.MessageEntry;
import osgi.msg.service.interfaces.MessageListener;
import osgi.msg.service.interfaces.MessageReaderService;

@Component(
	property = {
		"osgi.command.scope=log",
		"osgi.command.function=messages"},
	service=MessageCommand.class
)
public class MessageCommand {
	private MessageReaderService messageReader;
	
	private MessageListener listener;

	@Reference
	void addMessageReaderService(MessageReaderService service, Map<String, Object> properties) {
		this.messageReader = service;

//		System.out.println("Added " + service.getClass().getName());
//		
//		properties.forEach((k, v) -> {
//			System.out.println(k+"="+v);
//		});
//		
//		System.out.println();
	}

	void removeMessageReaderService(MessageReaderService service) {
		this.messageReader=null;
		
		System.out.println("Removed " + service.getClass().getName());
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
				
				System.out.println(entry.getTime() + " " + entry.getText());
			}
			
			break;
		}
    }
}