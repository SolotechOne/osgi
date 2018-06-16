package osgi.log.service.consumer;

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import osgi.msg.service.interfaces.MessageEntry;
import osgi.msg.service.interfaces.MessageListener;
import osgi.msg.service.interfaces.MessageService;

public class MessageEventWriter implements MessageListener {
	// constants for Event topic substring
	public static final String TOPIC = "osgi/msg/service/provider/LogEntry";
	public static final char TOPIC_SEPARATOR = '/';
	// constants for Event types
	public static final String MESSAGE_TYPE_INFO = "INFO";
	public static final String MESSAGE_TYPE_WARNING = "WARNING";
	public static final String MESSAGE_TYPE_ERROR = "ERROR";
	public static final String MESSAGE_TYPE_ABORT = "ABORT";
	public static final String MESSAGE_TYPE_QUESTION = "QUESTION";
	public static final String MESSAGE_TYPE_CAPTION = "CAPTION";
	// constants for Event properties
	public static final String SYSTEM = "system";
	public static final String TIME = "time";
	public static final String INSERT = "insert";
	public static final String NUMBER = "number";
	public static final String TEXT = "text";
	public static final String TYPE = "type";

	private EventAdmin eventAdmin;

    private BundleContext context;

    public MessageEventWriter(BundleContext context) {
    	this.context=context;
        
        ServiceReference ref = context.getServiceReference(EventAdmin.class.getName());
        if (ref != null) {
        	this.eventAdmin = (EventAdmin) context.getService(ref);
        }
    }
    
//	@Reference
//	void bindEventAdmin(EventAdmin service, Map<String, Object> properties) {
//		this.eventAdmin = service;
//
//		System.out.println("Added " + service.getClass().getName());
//	}
//
//	void unbindEventAdmin(EventAdmin service) {
//		this.eventAdmin=null;
//		
//		System.out.println("Removed " + service.getClass().getName());
//	}
//
//	@Activate
//    void activate(BundleContext context) {
//        System.out.println("EventWriter activated");
//        
//        this.context=context;
//        
//        System.out.println(context);
//    }
//
//    @Modified
//    void modified() {
//        System.out.println("EventWriter modified");
//    }
//
//    @Deactivate
//    void deactivate() {
//        System.out.println("EventWriter deactivated");
//    }
    
	@Override
	public void logged(MessageEntry entry) {
		String topic = TOPIC;
		
		switch (entry.getType()) {
		case MessageService.MESSAGE_TYPE_INFO:
			topic += TOPIC_SEPARATOR + MESSAGE_TYPE_INFO;
			break;
		case MessageService.MESSAGE_TYPE_WARNING:
			topic += TOPIC_SEPARATOR + MESSAGE_TYPE_WARNING;
			break;
		case MessageService.MESSAGE_TYPE_ERROR:
			topic += TOPIC_SEPARATOR + MESSAGE_TYPE_ERROR;
			break;
		case MessageService.MESSAGE_TYPE_ABORT:
			topic += TOPIC_SEPARATOR + MESSAGE_TYPE_ABORT;
			break;
		case MessageService.MESSAGE_TYPE_QUESTION:
			topic += TOPIC_SEPARATOR + MESSAGE_TYPE_QUESTION;
			break;
		default:
			topic += TOPIC_SEPARATOR + MESSAGE_TYPE_CAPTION;
		}
		
		Map<String, Object> properties = new HashMap<>();
		
		properties.put(SYSTEM, entry.getSystem());
		properties.put(TIME, new Long(entry.getTime()));
		properties.put(INSERT, entry.getInsert());
		properties.put(NUMBER, entry.getNumber());
		properties.put(TEXT, entry.getText());
		properties.put(TYPE, entry.getType());
		
		Event event = new Event(topic, properties);
		
		this.eventAdmin.postEvent(event);
	}
}