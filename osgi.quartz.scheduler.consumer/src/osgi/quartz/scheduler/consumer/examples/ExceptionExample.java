package osgi.quartz.scheduler.consumer.examples;

import java.util.Date;

import org.apache.log4j.Logger;

import static org.quartz.DateBuilder.nextGivenSecondDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

import osgi.quartz.scheduler.consumer.jobs.BadJob1;
import osgi.quartz.scheduler.consumer.jobs.BadJob2;

/**
 * This job demonstrates how Quartz can handle JobExecutionExceptions that are thrown by jobs.
 * 
 * @author Bill Kratzer
 */
public class ExceptionExample extends Thread {
	private Logger logger = Logger.getLogger(ExceptionExample.class);

	private Scheduler scheduler;

	@SuppressWarnings("unused")
	private volatile boolean active = true;

	public void run() {
		try {
			this.scheduler = StdSchedulerFactory.getDefaultScheduler();

			logger.info("------- Initialization Complete --------");

			Date nextStartTime;

			logger.info("------- Scheduling Jobs ----------------");

			// jobs can be scheduled before start() has been called

			// get a "nice round" time a few seconds in the future...
			Date startTime = nextGivenSecondDate(null, 15);

			// badJob1 will run every 10 seconds
			// this job will throw an exception and refire
			// immediately
			JobDetail job = newJob(BadJob1.class).withIdentity("badJob1", "group1").usingJobData("denominator", "0").build();

			SimpleTrigger trigger = newTrigger().withIdentity("trigger1", "group1").startAt(startTime)
					.withSchedule(simpleSchedule().withIntervalInSeconds(10).repeatForever()).build();

			nextStartTime = this.scheduler.scheduleJob(job, trigger);
			this.logger.info(job.getKey() + " will run at: " + nextStartTime + " and repeat: " + trigger.getRepeatCount() + " times, every "
					+ trigger.getRepeatInterval() / 1000 + " seconds");

			// badJob2 will run every five seconds
			// this job will throw an exception and never
			// refire
			job = newJob(BadJob2.class).withIdentity("badJob2", "group1").build();

			trigger = newTrigger().withIdentity("trigger2", "group1").startAt(startTime)
					.withSchedule(simpleSchedule().withIntervalInSeconds(5).repeatForever()).build();

			nextStartTime = this.scheduler.scheduleJob(job, trigger);
			this.logger.info(job.getKey() + " will run at: " + nextStartTime + " and repeat: " + trigger.getRepeatCount() + " times, every "
					+ trigger.getRepeatInterval() / 1000 + " seconds");

			this.logger.info("------- Starting Scheduler ----------------");

			// jobs don't start firing until start() has been called...
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