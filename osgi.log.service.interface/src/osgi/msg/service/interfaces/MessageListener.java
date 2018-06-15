package osgi.msg.service.interfaces;

import java.util.EventListener;

public abstract interface MessageListener extends EventListener {
	public abstract void logged(MessageEntry entry);
}