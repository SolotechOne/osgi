package osgi.quartz.scheduler.consumer.examples;

import java.util.Date;

import org.apache.log4j.Logger;

import static org.quartz.DateBuilder.futureDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import osgi.quartz.scheduler.consumer.jobs.TriggerEchoJob;

/**
 * This Example will demonstrate how Triggers are ordered by priority.
 */
public class PriorityExample extends Thread {
	private Logger logger = Logger.getLogger(SimpleExample.class);

	private Scheduler scheduler;

	@SuppressWarnings("unused")
	private volatile boolean active = true;

	public void run() {
		try {
			this.scheduler = StdSchedulerFactory.getDefaultScheduler();

			this.logger.info("------- Initialization Complete -----------");

			Date nextStartTime;

			this.logger.info("------- Scheduling Job  -------------------");


			JobDetail job = newJob(TriggerEchoJob.class).withIdentity("TriggerEchoJob").build();

			// All three triggers will fire their first time at the same time,
			// ordered by their priority, and then repeat once, firing in a
			// staggered order that therefore ignores priority.
			//
			// We should see the following firing order:
			// 1. Priority10Trigger15SecondRepeat
			// 2. Priority5Trigger10SecondRepeat
			// 3. Priority1Trigger5SecondRepeat
			// 4. Priority1Trigger5SecondRepeat
			// 5. Priority5Trigger10SecondRepeat
			// 6. Priority10Trigger15SecondRepeat

			// Calculate the start time of all triggers as 5 seconds from now
			Date startTime = futureDate(5, IntervalUnit.SECOND);

			// First trigger has priority of 1, and will repeat after 5 seconds
			Trigger trigger1 = newTrigger().withIdentity("Priority1Trigger5SecondRepeat").startAt(startTime)
					.withSchedule(simpleSchedule().withRepeatCount(1).withIntervalInSeconds(5)).withPriority(1).forJob(job).build();

			// Second trigger has default priority of 5 (default), and will repeat after 10 seconds
			Trigger trigger2 = newTrigger().withIdentity("Priority5Trigger10SecondRepeat").startAt(startTime)
					.withSchedule(simpleSchedule().withRepeatCount(1).withIntervalInSeconds(10)).forJob(job).build();

			// Third trigger has priority 10, and will repeat after 15 seconds
			Trigger trigger3 = newTrigger().withIdentity("Priority10Trigger15SecondRepeat").startAt(startTime)
					.withSchedule(simpleSchedule().withRepeatCount(1).withIntervalInSeconds(15)).withPriority(10).forJob(job).build();

			// Tell quartz to schedule the job using our trigger
			nextStartTime = this.scheduler.scheduleJob(job, trigger1);
			nextStartTime = this.scheduler.scheduleJob(trigger2);
			nextStartTime = this.scheduler.scheduleJob(trigger3);


			// Start up the scheduler (nothing can actually run until the
			// scheduler has been started)
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