package osgi.quartz.scheduler.consumer.examples;

import java.util.Date;

import org.apache.log4j.Logger;

import static org.quartz.DateBuilder.evenMinuteDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import osgi.quartz.scheduler.consumer.jobs.HelloWorldJob;

public class SimpleExample extends Thread {
	private Logger logger = Logger.getLogger(JobStateExample.class);

	private Scheduler scheduler;

	@SuppressWarnings("unused")
	private volatile boolean active = true;

	public void run() {
		try {
			this.scheduler = StdSchedulerFactory.getDefaultScheduler();

			this.logger.info("------- Initialization Complete --------");

			Date nextStartTime;

			this.logger.info("------- Scheduling Jobs ----------------");


			// define the job and tie it to our HelloWorldJob class
			JobDetail helloworldjob = newJob(HelloWorldJob.class).withIdentity("job.1", "group.1").build();
			// Trigger the job to run now, and then repeat every 20 seconds
			Trigger trigger = newTrigger().withIdentity("trigger.1", "group.1").startNow().withSchedule(simpleSchedule().withIntervalInSeconds(20).repeatForever()).build();
			// Tell quartz to schedule the job using our trigger
			nextStartTime = this.scheduler.scheduleJob(helloworldjob, trigger);

			this.logger.info(helloworldjob.getKey() + " will run at: " + nextStartTime);


			// computer a time that is on the next round minute
			Date runTime = evenMinuteDate(new Date());
			// Trigger the job to run on the next round minute
			Trigger roundMinute = newTrigger().withIdentity("trigger.2", "group.1").startAt(runTime).forJob(helloworldjob).build();
			// Tell quartz to schedule the job using our trigger
			nextStartTime = this.scheduler.scheduleJob(roundMinute);

			this.logger.info(helloworldjob.getKey() + " will run at: " + nextStartTime);


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