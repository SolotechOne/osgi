package osgi.quartz.scheduler.consumer.examples;

import java.util.Date;

import org.apache.log4j.Logger;

import static org.quartz.DateBuilder.futureDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.JobKey.jobKey;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.DateBuilder;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerMetaData;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

import osgi.quartz.scheduler.consumer.jobs.SimpleJob1;

public class SimpleTriggerExample {
	public void run() throws Exception {
		Logger logger = Logger.getLogger(SimpleExample.class);

		logger.info("------- Initializing -------------------");

		// First we must get a reference to a scheduler
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

		logger.info("------- Initialization Complete --------");

		Date nextStartTime;
		
		logger.info("------- Scheduling Jobs ----------------");

		// jobs can be scheduled before sched.start() has been called

		// get a "nice round" time a few seconds in the future...
		Date startTime = DateBuilder.nextGivenSecondDate(null, 15);

		// job1 will only fire once at date/time "ts"
		JobDetail job = newJob(SimpleJob1.class).withIdentity("job1", "group1").build();

		SimpleTrigger trigger = (SimpleTrigger) newTrigger().withIdentity("trigger1", "group1").startAt(startTime).build();

		// schedule it to run!
		nextStartTime = scheduler.scheduleJob(job, trigger);
		logger.info(job.getKey() + " will run at: " + nextStartTime + " and repeat: " + trigger.getRepeatCount() + " times, every "
				+ trigger.getRepeatInterval() / 1000 + " seconds");

		// job2 will only fire once at date/time "ts"
		job = newJob(SimpleJob1.class).withIdentity("job2", "group1").build();

		trigger = (SimpleTrigger) newTrigger().withIdentity("trigger2", "group1").startAt(startTime).build();

		nextStartTime = scheduler.scheduleJob(job, trigger);
		logger.info(job.getKey() + " will run at: " + nextStartTime + " and repeat: " + trigger.getRepeatCount() + " times, every "
				+ trigger.getRepeatInterval() / 1000 + " seconds");

		// job3 will run 11 times (run once and repeat 10 more times)
		// job3 will repeat every 10 seconds
		job = newJob(SimpleJob1.class).withIdentity("job3", "group1").build();

		trigger = newTrigger().withIdentity("trigger3", "group1").startAt(startTime)
				.withSchedule(simpleSchedule().withIntervalInSeconds(10).withRepeatCount(10)).build();

		nextStartTime = scheduler.scheduleJob(job, trigger);
		logger.info(job.getKey() + " will run at: " + nextStartTime + " and repeat: " + trigger.getRepeatCount() + " times, every "
				+ trigger.getRepeatInterval() / 1000 + " seconds");

		// the same job (job3) will be scheduled by a another trigger
		// this time will only repeat twice at a 70 second interval

		trigger = newTrigger().withIdentity("trigger3", "group2").startAt(startTime)
				.withSchedule(simpleSchedule().withIntervalInSeconds(10).withRepeatCount(2)).forJob(job).build();

		nextStartTime = scheduler.scheduleJob(trigger);
		logger.info(job.getKey() + " will [also] run at: " + nextStartTime + " and repeat: " + trigger.getRepeatCount()
		+ " times, every " + trigger.getRepeatInterval() / 1000 + " seconds");

		// job4 will run 6 times (run once and repeat 5 more times)
		// job4 will repeat every 10 seconds
		job = newJob(SimpleJob1.class).withIdentity("job4", "group1").build();

		trigger = newTrigger().withIdentity("trigger4", "group1").startAt(startTime)
				.withSchedule(simpleSchedule().withIntervalInSeconds(10).withRepeatCount(5)).build();

		nextStartTime = scheduler.scheduleJob(job, trigger);
		logger.info(job.getKey() + " will run at: " + nextStartTime + " and repeat: " + trigger.getRepeatCount() + " times, every "
				+ trigger.getRepeatInterval() / 1000 + " seconds");

		// job5 will run once, five minutes in the future
		job = newJob(SimpleJob1.class).withIdentity("job5", "group1").build();

		trigger = (SimpleTrigger) newTrigger().withIdentity("trigger5", "group1")
				.startAt(futureDate(5, IntervalUnit.MINUTE)).build();

		nextStartTime = scheduler.scheduleJob(job, trigger);
		logger.info(job.getKey() + " will run at: " + nextStartTime + " and repeat: " + trigger.getRepeatCount() + " times, every "
				+ trigger.getRepeatInterval() / 1000 + " seconds");

		// job6 will run indefinitely, every 40 seconds
		job = newJob(SimpleJob1.class).withIdentity("job6", "group1").build();

		trigger = newTrigger().withIdentity("trigger6", "group1").startAt(startTime)
				.withSchedule(simpleSchedule().withIntervalInSeconds(40).repeatForever()).build();

		nextStartTime = scheduler.scheduleJob(job, trigger);
		logger.info(job.getKey() + " will run at: " + nextStartTime + " and repeat: " + trigger.getRepeatCount() + " times, every "
				+ trigger.getRepeatInterval() / 1000 + " seconds");

		logger.info("------- Starting Scheduler ----------------");

		// All of the jobs have been added to the scheduler, but none of the jobs
		// will run until the scheduler has been started
		scheduler.start();

		logger.info("------- Started Scheduler -----------------");

		// jobs can also be scheduled after start() has been called...
		// job7 will repeat 20 times, repeat every five minutes
		job = newJob(SimpleJob1.class).withIdentity("job7", "group1").build();

		trigger = newTrigger().withIdentity("trigger7", "group1").startAt(startTime)
				.withSchedule(simpleSchedule().withIntervalInMinutes(5).withRepeatCount(20)).build();

		nextStartTime = scheduler.scheduleJob(job, trigger);
		logger.info(job.getKey() + " will run at: " + nextStartTime + " and repeat: " + trigger.getRepeatCount() + " times, every "
				+ trigger.getRepeatInterval() / 1000 + " seconds");

		// jobs can be fired directly... (rather than waiting for a trigger)
		job = newJob(SimpleJob1.class).withIdentity("job8", "group1").storeDurably().build();

		scheduler.addJob(job, true);

		logger.info("'Manually' triggering job8...");
		scheduler.triggerJob(jobKey("job8", "group1"));

		logger.info("------- Waiting 30 seconds... --------------");

		try {
			// wait 33 seconds to show jobs
			Thread.sleep(30L * 1000L);
			// executing...
		} catch (Exception e) {
			//
		}

		// jobs can be re-scheduled...
		// job 7 will run immediately and repeat 10 times for every second
		logger.info("------- Rescheduling... --------------------");
		trigger = newTrigger().withIdentity("trigger7", "group1").startAt(startTime)
				.withSchedule(simpleSchedule().withIntervalInMinutes(5).withRepeatCount(20)).build();

		nextStartTime = scheduler.rescheduleJob(trigger.getKey(), trigger);
		logger.info("job7 rescheduled to run at: " + nextStartTime);

		logger.info("------- Waiting five minutes... ------------");
		try {
			// wait five minutes to show jobs
			Thread.sleep(300L * 1000L);
			// executing...
		} catch (Exception e) {
			//
		}

		logger.info("------- Shutting Down ---------------------");

		scheduler.shutdown(true);

		logger.info("------- Shutdown Complete -----------------");

		// display some stats about the schedule that just ran
		SchedulerMetaData metaData = scheduler.getMetaData();
		logger.info("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");

	}
}