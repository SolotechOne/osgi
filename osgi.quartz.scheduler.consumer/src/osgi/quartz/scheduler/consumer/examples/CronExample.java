package osgi.quartz.scheduler.consumer.examples;

import java.util.Date;

import org.apache.log4j.Logger;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;
import org.quartz.impl.StdSchedulerFactory;

import osgi.quartz.scheduler.consumer.jobs.SimpleJob1;

/**
 * This Example will demonstrate all of the basics of scheduling capabilities of Quartz using Cron Triggers.
 * 
 * @author Bill Kratzer
 */
public class CronExample extends Thread {
	private Logger logger = Logger.getLogger(CronExample.class);

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


			// jobs can be scheduled before sched.start() has been called

			// job 1 will run every 20 seconds
			JobDetail job = newJob(SimpleJob1.class).withIdentity("job1", "group1").build();

			CronTrigger trigger = newTrigger().withIdentity("trigger1", "group1").withSchedule(cronSchedule("0/20 * * * * ?"))
					.build();

			nextStartTime = this.scheduler.scheduleJob(job, trigger);
			this.logger.info(job.getKey() + " has been scheduled to run at: " + nextStartTime + " and repeat based on expression: "
					+ trigger.getCronExpression());

			// job 2 will run every other minute (at 15 seconds past the minute)
			job = newJob(SimpleJob1.class).withIdentity("job2", "group1").build();

			trigger = newTrigger().withIdentity("trigger2", "group1").withSchedule(cronSchedule("15 0/2 * * * ?")).build();

			nextStartTime = this.scheduler.scheduleJob(job, trigger);
			this.logger.info(job.getKey() + " has been scheduled to run at: " + nextStartTime + " and repeat based on expression: "
					+ trigger.getCronExpression());

			// job 3 will run every other minute but only between 8am and 5pm
			job = newJob(SimpleJob1.class).withIdentity("job3", "group1").build();

			trigger = newTrigger().withIdentity("trigger3", "group1").withSchedule(cronSchedule("0 0/2 8-17 * * ?")).build();

			nextStartTime = this.scheduler.scheduleJob(job, trigger);
			this.logger.info(job.getKey() + " has been scheduled to run at: " + nextStartTime + " and repeat based on expression: "
					+ trigger.getCronExpression());

			// job 4 will run every three minutes but only between 5pm and 11pm
			job = newJob(SimpleJob1.class).withIdentity("job4", "group1").build();

			trigger = newTrigger().withIdentity("trigger4", "group1").withSchedule(cronSchedule("0 0/3 17-23 * * ?")).build();

			nextStartTime = this.scheduler.scheduleJob(job, trigger);
			this.logger.info(job.getKey() + " has been scheduled to run at: " + nextStartTime + " and repeat based on expression: "
					+ trigger.getCronExpression());

			// job 5 will run at 10am on the 1st and 15th days of the month
			job = newJob(SimpleJob1.class).withIdentity("job5", "group1").build();

			trigger = newTrigger().withIdentity("trigger5", "group1").withSchedule(cronSchedule("0 0 10am 1,15 * ?")).build();

			nextStartTime = this.scheduler.scheduleJob(job, trigger);
			this.logger.info(job.getKey() + " has been scheduled to run at: " + nextStartTime + " and repeat based on expression: "
					+ trigger.getCronExpression());

			// job 6 will run every 30 seconds but only on Weekdays (Monday through Friday)
			job = newJob(SimpleJob1.class).withIdentity("job6", "group1").build();

			trigger = newTrigger().withIdentity("trigger6", "group1").withSchedule(cronSchedule("0,30 * * ? * MON-FRI"))
					.build();

			nextStartTime = this.scheduler.scheduleJob(job, trigger);
			this.logger.info(job.getKey() + " has been scheduled to run at: " + nextStartTime + " and repeat based on expression: "
					+ trigger.getCronExpression());

			// job 7 will run every 30 seconds but only on Weekends (Saturday and Sunday)
			job = newJob(SimpleJob1.class).withIdentity("job7", "group1").build();

			trigger = newTrigger().withIdentity("trigger7", "group1").withSchedule(cronSchedule("0,30 * * ? * SAT,SUN"))
					.build();

			nextStartTime = this.scheduler.scheduleJob(job, trigger);
			this.logger.info(job.getKey() + " has been scheduled to run at: " + nextStartTime + " and repeat based on expression: "
					+ trigger.getCronExpression());

			this.logger.info("------- Starting Scheduler ----------------");

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