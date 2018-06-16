package osgi.msg.service.interfaces;

import java.util.Enumeration;

public abstract interface MessageReaderService {
	public abstract void addMessageListener(MessageListener listener);
	
	public abstract void removeMessageListener(MessageListener listener);
	
	public abstract Enumeration<MessageEntry> getLog();
}