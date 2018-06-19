package osgi.quartz.scheduler.consumer.examples;

import java.util.Date;

import org.apache.log4j.Logger;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.JobListener;
import org.quartz.Matcher;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;
import org.quartz.Trigger;

import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.KeyMatcher;

import osgi.quartz.scheduler.consumer.jobs.SimpleJob1;
import osgi.quartz.scheduler.consumer.listener.Job1Listener;

/**
 * Demonstrates the behavior of <code>JobListener</code>s. In particular, this example will use a job listener to
 * trigger another job after one job succesfully executes.
 */
public class ListenerExample extends Thread {
	private Logger logger = Logger.getLogger(SimpleExample.class);
	
	private Scheduler scheduler;
	
	@SuppressWarnings("unused")
	private volatile boolean active = true;
	
	public void run() {
		this.logger.info("------- Initializing ----------------------");

		try {
			this.scheduler = StdSchedulerFactory.getDefaultScheduler();

			this.logger.info("------- Initialization Complete -----------");

			Date nextStartTime;

			this.logger.info("------- Scheduling Job  -------------------");


			// define the job and tie it to our SimpleJob1 class
			JobDetail job = newJob(SimpleJob1.class).withIdentity("job1").build();
			// trigger the job to run immediately
			Trigger trigger = newTrigger().withIdentity("trigger1").startNow().build();

			// Set up the listener
			JobListener listener = new Job1Listener();
			Matcher<JobKey> matcher = KeyMatcher.keyEquals(job.getKey());
			this.scheduler.getListenerManager().addJobListener(listener, matcher);

			// tell quartz to schedule the job using our trigger
			nextStartTime = scheduler.scheduleJob(job, trigger);

			this.logger.info(job.getKey() + " will run at: " + nextStartTime);
			
			
			// Start up the scheduler (nothing can actually run until the scheduler has been started)
			this.scheduler.start();

			this.logger.info("------- Started Scheduler -----------------");
		} catch (SchedulerException exception) {
			exception.printStackTrace();
		}
	}
	
    public void stopThread() {
    	this.active = false;
    	
		// shut down the scheduler
		this.logger.info("------- Shutting Down ---------------------");

		try {
			this.scheduler.shutdown(true);

			this.logger.info("------- Shutdown Complete -----------------");

			// display some stats about the schedule that just ran
			SchedulerMetaData metaData = this.scheduler.getMetaData();
			
			this.logger.info("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
		} catch (SchedulerException exception) {
			exception.printStackTrace();
		}
    }
}