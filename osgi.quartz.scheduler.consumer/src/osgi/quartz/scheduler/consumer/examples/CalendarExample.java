package osgi.quartz.scheduler.consumer.examples;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

import static org.quartz.DateBuilder.dateOf;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerMetaData;
import org.quartz.SimpleTrigger;

import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.calendar.AnnualCalendar;

import osgi.quartz.scheduler.consumer.jobs.SimpleJob1;

/**
 * This example will demonstrate how calendars can be used to exclude periods of time when scheduling should not take
 * place.
 */
public class CalendarExample {
	public void run() throws Exception {
		Logger logger = Logger.getLogger(SimpleExample.class);

		logger.info("------- Initializing ----------------------");

		// First we must get a reference to a scheduler
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

		logger.info("------- Initialization Complete -----------");

		logger.info("------- Scheduling Jobs -------------------");

		// Add the holiday calendar to the schedule
		AnnualCalendar holidays = new AnnualCalendar();

		// fourth of July (July 4)
		Calendar fourthOfJuly = new GregorianCalendar(2005, 6, 4);
		holidays.setDayExcluded(fourthOfJuly, true);
		// halloween (Oct 31)
		Calendar halloween = new GregorianCalendar(2005, 9, 31);
		holidays.setDayExcluded(halloween, true);
		// christmas (Dec 25)
		Calendar christmas = new GregorianCalendar(2005, 11, 25);
		holidays.setDayExcluded(christmas, true);

		// tell the schedule about our holiday calendar
		scheduler.addCalendar("holidays", holidays, false, false);

		// schedule a job to run hourly, starting on halloween
		// at 10 am
		Date runDate = dateOf(0, 0, 10, 31, 10);

		JobDetail job = newJob(SimpleJob1.class).withIdentity("job1", "group1").build();

		SimpleTrigger trigger = newTrigger().withIdentity("trigger1", "group1").startAt(runDate)
				.withSchedule(simpleSchedule().withIntervalInHours(1).repeatForever()).modifiedByCalendar("holidays").build();

		// schedule the job and print the first run date
		Date firstRunTime = scheduler.scheduleJob(job, trigger);

		// print out the first execution date.
		// Note: Since Halloween (Oct 31) is a holiday, then
		// we will not run until the next day! (Nov 1)
		logger.info(job.getKey() + " will run at: " + firstRunTime + " and repeat: " + trigger.getRepeatCount()
		+ " times, every " + trigger.getRepeatInterval() / 1000 + " seconds");

		// All of the jobs have been added to the scheduler, but none of the jobs
		// will run until the scheduler has been started
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