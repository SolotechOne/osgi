package osgi.quartz.scheduler.consumer.examples;

import java.util.Date;

import org.apache.log4j.Logger;

import static org.quartz.DateBuilder.evenMinuteDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerMetaData;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import osgi.quartz.scheduler.consumer.jobs.HelloWorldJob;

public class SimpleExample {
	public void run() throws Exception {
		Logger logger = Logger.getLogger(SimpleExample.class);

		logger.info("------- Initializing ----------------------");

		// First we must get a reference to a scheduler
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

		logger.info("------- Initialization Complete -----------");

		Date nextStartTime;

		logger.info("------- Scheduling Job  -------------------");

		// define the job and tie it to our HelloWorldJob class
		JobDetail helloworldjob = newJob(HelloWorldJob.class).withIdentity("job.1", "group.1").build();
		// Trigger the job to run now, and then repeat every 20 seconds
		Trigger trigger = newTrigger().withIdentity("trigger.1", "group.1").startNow().withSchedule(simpleSchedule().withIntervalInSeconds(20).repeatForever()).build();
		// Tell quartz to schedule the job using our trigger
		nextStartTime = scheduler.scheduleJob(helloworldjob, trigger);

		logger.info(helloworldjob.getKey() + " will run at: " + nextStartTime);


		// computer a time that is on the next round minute
		Date runTime = evenMinuteDate(new Date());
		// Trigger the job to run on the next round minute
		Trigger roundMinute = newTrigger().withIdentity("trigger.2", "group.1").startAt(runTime).forJob(helloworldjob).build();
		// Tell quartz to schedule the job using our trigger
		nextStartTime = scheduler.scheduleJob(roundMinute);

		logger.info(helloworldjob.getKey() + " will run at: " + nextStartTime);


		// Start up the scheduler (nothing can actually run until the scheduler has been started)
		scheduler.start();

		logger.info("------- Started Scheduler -----------------");

		// wait long enough so that the scheduler as an opportunity to run the job!
		logger.info("------- Waiting 65 seconds... -------------");

		try {
			// wait 65 seconds to show job
			Thread.sleep(65l*1000l);
			// executing...
		} catch (Exception e) {
			//
		}

		// shut down the scheduler
		logger.info("------- Shutting Down ---------------------");

		scheduler.shutdown(true);

		logger.info("------- Shutdown Complete -----------------");


		// display some stats about the schedule that just ran
		SchedulerMetaData metaData = scheduler.getMetaData();
		System.out.println("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
	}
}