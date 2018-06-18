package osgi.quartz.scheduler.consumer.listener;

import org.apache.log4j.Logger;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import osgi.quartz.scheduler.consumer.examples.SimpleExample;
import osgi.quartz.scheduler.consumer.jobs.SimpleJob2;

public class Job1Listener implements JobListener {
	private static Logger logger = Logger.getLogger(SimpleExample.class);

	public String getName() {
		return "job1_to_job2";
	}

	public void jobToBeExecuted(JobExecutionContext context) {
		logger.info("Job1Listener says: Job Is about to be executed.");
	}

	public void jobExecutionVetoed(JobExecutionContext context) {
		logger.info("Job1Listener says: Job Execution was vetoed.");
	}

	public void jobWasExecuted(JobExecutionContext context, JobExecutionException exception) {
		logger.info("Job1Listener says: Job was executed.");

		// Simple job #2
		JobDetail job2 = newJob(SimpleJob2.class).withIdentity("job2").build();

		Trigger trigger = newTrigger().withIdentity("job2Trigger").startNow().build();

		try {
			// schedule the job to run!
			context.getScheduler().scheduleJob(job2, trigger);
		} catch (SchedulerException e) {
			logger.warn("Unable to schedule job2!");
			e.printStackTrace();
		}
	}
}