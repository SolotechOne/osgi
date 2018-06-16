package osgi.msg.service.interfaces;

import java.util.Iterator;

public abstract interface MessageReaderService {
	public abstract void addMessageListener(MessageListener listener);
	
	public abstract void removeMessageListener(MessageListener listener);
	
	public abstract Iterator<MessageEntry> getLog();
}