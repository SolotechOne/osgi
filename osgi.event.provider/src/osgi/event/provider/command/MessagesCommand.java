package osgi.event.provider.command;

import java.io.IOException;

import org.apache.felix.service.command.Descriptor;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import osgi.event.consumer.MessageConsumer;
import osgi.event.implementation.Messages;
import osgi.event.internal.IMessages;

//@Component(
//	property = {
//	    "osgi.command.scope=event",
//	    "osgi.command.function=messages"
//	},
//	service=MessagesCommand.class
//)
public class MessagesCommand {
	MessageConsumer mc;
	
	private IMessages messages;
	
//	@Reference
//    void setMessageConsumer(MessageConsumer mc) {
//        this.mc = mc;
//        
//        System.out.println(mc);
//    }
	
	@Activate
    void activate(BundleContext context) {
//        System.out.println("MessagesCommand activated");
        
//    	get single instance of Connection service
//    	System.out.println("------ single service reference --------");
    	@SuppressWarnings("unchecked")
		ServiceReference<Messages> sr = (ServiceReference<Messages>) context.getServiceReference("osgi.event.implementation.Messages");
//    	System.out.println(sr);
    	
    	this.messages = (Messages) context.getService(sr);
    }

    @Modified
    void modified() {
        System.out.println("MessagesCommand modified");
    }

    @Deactivate
    void deactivate() {
        System.out.println("MessagesCommand deactivated");
    }

	@Descriptor("show messages")
	public void messages() throws IOException {
		this.messages.list();
    }
}
