package osgi.quartz.scheduler.consumer;

import osgi.quartz.scheduler.consumer.examples.CalendarExample;
import osgi.quartz.scheduler.consumer.examples.CronExample;
import osgi.quartz.scheduler.consumer.examples.ExceptionExample;
import osgi.quartz.scheduler.consumer.examples.InterruptExample;
import osgi.quartz.scheduler.consumer.examples.JobStateExample;
import osgi.quartz.scheduler.consumer.examples.ListenerExample;
import osgi.quartz.scheduler.consumer.examples.MsgExample;
import osgi.quartz.scheduler.consumer.examples.PriorityExample;
import osgi.quartz.scheduler.consumer.examples.SimpleExample;
import osgi.quartz.scheduler.consumer.examples.TriggerExample;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	private SimpleExample simpleExample;
	private TriggerExample simpleTriggerExample;
	private ListenerExample listenerExample;
	private PriorityExample priorityExample;
	private ExceptionExample exceptionExample;
	private JobStateExample jobStateExample;
	private CalendarExample calendarExample;
	private InterruptExample interruptExample;
	private CronExample cronExample;
	
	private MsgExample msgExample;

	private static BundleContext context;
	
	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
//		this.simpleExample = new SimpleExample();
//		this.simpleExample.run();
		
//		this.triggerExample = new TriggerExample();
//		this.triggerExample.run();
		
//		this.listenerExample = new ListenerExample();
//		this.listenerExample.start();

//		this.priorityExample = new PriorityExample();
//		this.priorityExample.start();

//		this.exceptionExample = new ExceptionExample();
//		this.exceptionExample.run();

//		this.jobStateExample = new JobStateExample();
//		this.jobStateExample.run();

//		this.calendarExample = new CalendarExample();
//		this.calendarExample.run();

//		this.interruptExample = new InterruptExample();
//		this.interruptExample.run();
		
//		this.cronExample = new CronExample();
//		this.cronExample.run();
		
		this.msgExample = new MsgExample(bundleContext);
		this.msgExample.run();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		this.msgExample.stopThread();
		this.msgExample.join();
		
//		this.cronExample.stopThread();
//		this.cronExample.join();
		
//		this.interruptExample.stopThread();
//		this.interruptExample.join();

//		this.calendarExample.stopThread();
//		this.calendarExample.join();
		
//		this.jobStateExample.stopThread();
//		this.jobStateExample.join();

//		this.exceptionExample.stopThread();
//		this.exceptionExample.join();
		
//		this.priorityExample.stopThread();
//		this.priorityExample.join();
		
//		this.listenerExample.stopThread();
//		this.listenerExample.join();
		
//		this.triggerExample.stopThread();
//		this.triggerExample.join();

//		this.simpleExample.stopThread();
//		this.simpleExample.join();
		
		Activator.context = null;
	}
}