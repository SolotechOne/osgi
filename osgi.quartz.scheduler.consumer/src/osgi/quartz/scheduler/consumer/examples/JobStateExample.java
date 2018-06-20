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

import osgi.quartz.scheduler.consumer.jobs.ColorJob;

/**
 * This Example will demonstrate how job parameters can be passed into jobs and how state can be maintained
 * 
 * @author Bill Kratzer
 */
public class JobStateExample extends Thread {
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

			// get a "nice round" time a few seconds in the future....
			Date startTime = nextGivenSecondDate(null, 10);

			// job1 will only run 5 times (at start time, plus 4 repeats), every 10 seconds
			JobDetail job1 = newJob(ColorJob.class).withIdentity("job1", "group1").build();

			SimpleTrigger trigger1 = newTrigger().withIdentity("trigger1", "group1").startAt(startTime)
					.withSchedule(simpleSchedule().withIntervalInSeconds(10).withRepeatCount(4)).build();

			// pass initialization parameters into the job
			job1.getJobDataMap().put(ColorJob.FAVORITE_COLOR, "Green");
			job1.getJobDataMap().put(ColorJob.EXECUTION_COUNT, 1);

			// schedule the job to run
			Date scheduleTime1 = this.scheduler.scheduleJob(job1, trigger1);

			this.logger.info(job1.getKey() + " will run at: " + scheduleTime1 + " and repeat: " + trigger1.getRepeatCount() + " times, every "
					+ trigger1.getRepeatInterval() / 1000 + " seconds");

			// job2 will also run 5 times, every 10 seconds
			JobDetail job2 = newJob(ColorJob.class).withIdentity("job2", "group1").build();

			SimpleTrigger trigger2 = newTrigger().withIdentity("trigger2", "group1").startAt(startTime)
					.withSchedule(simpleSchedule().withIntervalInSeconds(10).withRepeatCount(4)).build();

			// pass initialization parameters into the job
			// this job has a different favorite color!
			job2.getJobDataMap().put(ColorJob.FAVORITE_COLOR, "Red");
			job2.getJobDataMap().put(ColorJob.EXECUTION_COUNT, 1);

			// schedule the job to run
			Date scheduleTime2 = this.scheduler.scheduleJob(job2, trigger2);
			this.logger.info(job2.getKey().toString() + " will run at: " + scheduleTime2 + " and repeat: " + trigger2.getRepeatCount()
			+ " times, every " + trigger2.getRepeatInterval() / 1000 + " seconds");

			this.logger.info("------- Starting Scheduler ----------------");

			// All of the jobs have been added to the scheduler, but none of the jobs
			// will run until the scheduler has been started
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