package osgi.quartz.scheduler.consumer.jobs;

import java.util.Date;

import org.apache.log4j.Logger;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

/**
 * <p>
 * This is just a simple job that gets fired off many times by example 1
 * </p>
 * 
 * @author Bill Kratzer
 */
public class DelayJob implements Job {
	private static Logger logger = Logger.getLogger(DelayJob.class);

	// job parameter
	public static final String DELAY_TIME = "delay time";

	/**
	 * Empty constructor for job initilization
	 */
	public DelayJob() {
	}

	/**
	 * <p>
	 * Called by the <code>{@link org.quartz.Scheduler}</code> when a <code>{@link org.quartz.Trigger}</code> fires that
	 * is associated with the <code>Job</code>.
	 * </p>
	 * 
	 * @throws JobExecutionException if there is an exception while executing the job.
	 */
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// This job simply prints out its job name and the date and time that it is running
		JobKey jobKey = context.getJobDetail().getKey();
		
		logger.info("Executing job: " + jobKey + " executing at " + new Date());

		// wait for a period of time
		long delayTime = context.getJobDetail().getJobDataMap().getLong(DELAY_TIME);
		
		try {
			Thread.sleep(delayTime);
		} catch (Exception exception) {
		}

		logger.info("Finished Executing job: " + jobKey + " at " + new Date());
	}
}