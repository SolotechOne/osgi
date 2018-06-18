package osgi.log.service.consumer;

import org.osgi.framework.BundleContext;

import org.osgi.util.tracker.ServiceTracker;

import osgi.msg.service.interfaces.MessageService;

public class MessageThread extends Thread {
	private volatile boolean active = true;
	
	private BundleContext context;
	
    public MessageThread(BundleContext context) {
		this.context = context;
	}
    
    public void run() {
    	while (active) {
    		ServiceTracker messageServiceTracker = new ServiceTracker(context, osgi.msg.service.interfaces.MessageService.class.getName(), null);

    		messageServiceTracker.open();

    		MessageService messageservice = (MessageService) messageServiceTracker.getService();

    		if (messageservice != null)
    			messageservice.log("aedev", MessageService.MESSAGE_TYPE_INFO, 98273, "Job 'JOBS.API@HELLO$WORLD' ended successfully.", "|I|0098273|JOBS.API@HELLO$WORLD");
    		else
    			System.out.println("no logging service");

    		messageServiceTracker.close();

    		try {
    			Thread.sleep(5000);
    		} catch (Exception exception) {
    			System.out.println("Thread interrupted " + exception.getMessage());
    		}
    	}
    }

    public void stopThread() {
    	active = false;
    }
}