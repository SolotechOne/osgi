package osgi.quartz.scheduler.consumer;

import osgi.quartz.scheduler.consumer.examples.CalendarExample;
import osgi.quartz.scheduler.consumer.examples.InterruptExample;
import osgi.quartz.scheduler.consumer.examples.ListenerExample;
import osgi.quartz.scheduler.consumer.examples.PriorityExample;
import osgi.quartz.scheduler.consumer.examples.SimpleExample;
import osgi.quartz.scheduler.consumer.examples.SimpleTriggerExample;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	private static BundleContext context;
	
	private ListenerExample listenerExample;
	
	private PriorityExample priorityExample;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
//		SimpleExample simpleExample = new SimpleExample();
//		simpleExample.run();
		
		
//		SimpleTriggerExample simpleTriggerExample = new SimpleTriggerExample();
//		simpleTriggerExample.run();

	
//		CalendarExample calendarExample = new CalendarExample();
//		calendarExample.run();

	
//		InterruptExample interruptExample = new InterruptExample();
//		interruptExample.run();

	
//		this.listenerExample = new ListenerExample();
//		this.listenerExample.start();
		
		this.priorityExample = new PriorityExample();
		this.priorityExample.start();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		this.priorityExample.stopThread();
		this.priorityExample.join();
		
//		this.listenerExample.stopThread();
//		this.listenerExample.join();
		
		Activator.context = null;
	}
}