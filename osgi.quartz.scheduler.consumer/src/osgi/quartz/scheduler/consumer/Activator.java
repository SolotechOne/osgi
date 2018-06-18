package osgi.quartz.scheduler.consumer;

import static org.quartz.DateBuilder.evenMinuteDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

import org.quartz.impl.StdSchedulerFactory;

import osgi.quartz.scheduler.consumer.jobs.HelloWorldJob;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerMetaData;
import org.quartz.Trigger;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		Date startTime;
		
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		
		// define the job and tie it to our HelloWorldJob class
		JobDetail job = newJob(HelloWorldJob.class).withIdentity("job1", "group1").build();
		// Trigger the job to run now, and then repeat every 20 seconds
		Trigger trigger = newTrigger().withIdentity("trigger1", "group1").startNow().withSchedule(simpleSchedule().withIntervalInSeconds(20).repeatForever()).build();
		// Tell quartz to schedule the job using our trigger
		startTime = scheduler.scheduleJob(job, trigger);
		
		
		// computer a time that is on the next round minute
		Date runTime = evenMinuteDate(new Date());
		// Trigger the job to run on the next round minute
		Trigger roundMinute = newTrigger().withIdentity("trigger2", "group1").startAt(runTime).forJob(job).build();
		// Tell quartz to schedule the job using our trigger
		startTime = scheduler.scheduleJob(roundMinute);
		
		
		scheduler.start();
		
		
		Thread.sleep(65l * 1000l);
		
		
		scheduler.shutdown();
		
		// display some stats about the schedule that just ran
	    SchedulerMetaData metaData = scheduler.getMetaData();
	    System.out.println("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
	}

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}
}