package osgi.quartz.scheduler.consumer.examples;

import java.util.Date;

import org.apache.log4j.Logger;

import static org.quartz.DateBuilder.nextGivenSecondDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerMetaData;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

import osgi.quartz.scheduler.consumer.jobs.DumbInterruptableJob;

/**
 * Demonstrates the behavior of <code>StatefulJob</code>s, as well as how misfire instructions affect the firings of
 * triggers of <code>StatefulJob</code> s - when the jobs take longer to execute that the frequency of the trigger's
 * repitition.
 * <p>
 * While the example is running, you should note that there are two triggers with identical schedules, firing identical
 * jobs. The triggers "want" to fire every 3 seconds, but the jobs take 10 seconds to execute. Therefore, by the time
 * the jobs complete their execution, the triggers have already "misfired" (unless the scheduler's "misfire threshold"
 * has been set to more than 7 seconds). You should see that one of the jobs has its misfire instruction set to
 * <code>SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_EXISTING_REPEAT_COUNT</code>- which causes it to fire
 * immediately, when the misfire is detected. The other trigger uses the default "smart policy" misfire instruction,
 * which causes the trigger to advance to its next fire time (skipping those that it has missed) - so that it does not
 * refire immediately, but rather at the next scheduled time.
 * </p>
 * 
 * @author <a href="mailto:bonhamcm@thirdeyeconsulting.com">Chris Bonham</a>
 */
public class InterruptExample {
	public void run() throws Exception {
		Logger logger = Logger.getLogger(InterruptExample.class);

		logger.info("------- Initializing ----------------------");

		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

		logger.info("------- Initialization Complete -----------");

		Date nextStartTime;

		logger.info("------- Scheduling Job  -------------------");

		// get a "nice round" time a few seconds in the future...
		Date runTime = nextGivenSecondDate(null, 15);

		JobDetail job = newJob(DumbInterruptableJob.class).withIdentity("interruptableJob1", "group1").build();

		SimpleTrigger trigger = newTrigger().withIdentity("trigger1", "group1").startAt(runTime)
				.withSchedule(simpleSchedule().withIntervalInSeconds(5).repeatForever()).build();

		nextStartTime = scheduler.scheduleJob(job, trigger);

		logger.info(job.getKey() + " will run at: " + nextStartTime + " and repeat: " + trigger.getRepeatCount() + " times, every "
				+ trigger.getRepeatInterval() / 1000 + " seconds");


		// Start up the scheduler (nothing can actually run until the scheduler has been started)
		scheduler.start();

		logger.info("------- Started Scheduler -----------------");

		logger.info("------- Starting loop to interrupt job every 7 seconds ----------");

		for (int i = 0; i < 5; i++) {
			try {
				Thread.sleep(7000L);
				// tell the scheduler to interrupt our job
				scheduler.interrupt(job.getKey());
			} catch (Exception e) {
				//
			}
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