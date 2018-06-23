package osgi.quartz.scheduler.consumer.examples;

import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.osgi.framework.BundleContext;

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

import osgi.quartz.scheduler.consumer.jobs.MsgJob;

public class MsgExample extends Thread {
	private Logger logger = Logger.getLogger(MsgExample.class);

	private BundleContext context;
	
	private Scheduler scheduler;

	@SuppressWarnings("unused")
	private volatile boolean active = true;

	public MsgExample(BundleContext context) {
		this.context = context;
	}
	
	public void run() {
		try {
			StdSchedulerFactory factory = new StdSchedulerFactory();

			Properties schedulerProperties = new Properties();
			schedulerProperties.setProperty("org.quartz.scheduler.instanceName", "DefaultQuartzScheduler");
			schedulerProperties.setProperty("org.quartz.scheduler.rmi.export", "false");
			schedulerProperties.setProperty("org.quartz.scheduler.rmi.proxy", "false");
			schedulerProperties.setProperty("org.quartz.scheduler.wrapJobExecutionInUserTransaction", "false");
			schedulerProperties.setProperty("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
			schedulerProperties.setProperty("org.quartz.threadPool.threadCount", "3");
			schedulerProperties.setProperty("org.quartz.threadPool.threadPriority", "5");
			schedulerProperties.setProperty("org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread", "true");
			schedulerProperties.setProperty("org.quartz.jobStore.misfireThreshold", "60000");
			schedulerProperties.setProperty("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");
			
			factory.initialize(schedulerProperties);

			this.scheduler = factory.getScheduler();
			
			this.logger.info("------- Initialization Complete --------");

			Date nextStartTime;

			this.logger.info("------- Scheduling Jobs ----------------");


			// define the job and tie it to our HelloWorldJob class
			JobDetail msgJob = newJob(MsgJob.class).withIdentity("job.1", "group.1").build();
			// Trigger the job to run now, and then repeat every 20 seconds
			Trigger trigger = newTrigger().withIdentity("trigger.1", "group.1").startNow().withSchedule(simpleSchedule().withIntervalInSeconds(20).repeatForever()).build();
			// Tell quartz to schedule the job using our trigger
			nextStartTime = this.scheduler.scheduleJob(msgJob, trigger);

			this.logger.info(msgJob.getKey() + " will run at: " + nextStartTime);


			// computer a time that is on the next round minute
			Date runTime = evenMinuteDate(new Date());
			// Trigger the job to run on the next round minute
			Trigger roundMinute = newTrigger().withIdentity("trigger.2", "group.1").startAt(runTime).forJob(msgJob).build();
			// Tell quartz to schedule the job using our trigger
			nextStartTime = this.scheduler.scheduleJob(roundMinute);

			this.logger.info(msgJob.getKey() + " will run at: " + nextStartTime);


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