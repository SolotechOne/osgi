package osgi.log.service.consumer;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import osgi.log.service.interfaces.LogReaderService;
import osgi.log.service.interfaces.LogService;
import osgi.msg.service.interfaces.MessageReaderService;

public class Activator implements BundleActivator {
	private BundleContext context;
	
	private BundleContext getContext() {
		return context;
	}

	private MessageThread thread;
	
	@Override
	public void start(BundleContext context) throws Exception {
		this.context = context;

		ServiceReference readref = context.getServiceReference(osgi.log.service.interfaces.LogReaderService.class.getName());

		if (readref != null) {
			LogReaderService reader = (LogReaderService) context.getService(readref);
			reader.addLogListener(new LogWriter());
		}

		ServiceReference logref = context.getServiceReference(osgi.log.service.interfaces.LogService.class.getName());

		if (logref != null) {
			LogService log = (LogService) context.getService(logref);

			log.log(LogService.LOG_INFO, "fuck");
		}

		
		ServiceReference messagereadref = context.getServiceReference(osgi.msg.service.interfaces.MessageReaderService.class.getName());
		
		if (messagereadref != null) {
			MessageReaderService message = (MessageReaderService) context.getService(messagereadref);
		    message.addMessageListener(new MessageWriter());
		}

		
		this.thread = new MessageThread(context);
		thread.start();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		this.context = null;
		
		thread.stopThread();
		thread.join();
	}
}