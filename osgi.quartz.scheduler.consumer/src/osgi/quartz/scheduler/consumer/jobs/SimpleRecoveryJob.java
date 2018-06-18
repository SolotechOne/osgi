package osgi.quartz.scheduler.consumer.jobs;

import java.util.Date;

import org.apache.log4j.Logger;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

/**
 * <p>
 * A dumb implementation of Job, for unit testing purposes.
 * </p>
 * 
 * @author James House
 */
public class SimpleRecoveryJob implements Job {
	private static Logger logger = Logger.getLogger(SimpleRecoveryJob.class);

	private static final String COUNT = "count";

	/**
	 * Quartz requires a public empty constructor so that the scheduler can instantiate the class whenever it needs.
	 */
	public SimpleRecoveryJob() {
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
		JobKey jobKey = context.getJobDetail().getKey();

		// if the job is recovering print a message
		if (context.isRecovering()) {
			logger.info("SimpleRecoveryJob: " + jobKey + " RECOVERING at " + new Date());
		} else {
			logger.info("SimpleRecoveryJob: " + jobKey + " starting at " + new Date());
		}

		// delay for ten seconds
		long delay = 10l * 1000l;
		
		try {
			Thread.sleep(delay);
		} catch (Exception e) {
			//
		}

		JobDataMap data = context.getJobDetail().getJobDataMap();
		
		int count;
		
		if (data.containsKey(COUNT)) {
			count = data.getInt(COUNT);
		} else {
			count = 0;
		}
		
		count++;
		
		data.put(COUNT, count);

		logger.info("SimpleRecoveryJob: " + jobKey + " done at " + new Date() + "\n Execution #" + count);
	}
}