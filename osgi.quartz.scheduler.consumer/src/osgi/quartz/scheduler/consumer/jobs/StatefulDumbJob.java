package osgi.quartz.scheduler.consumer.jobs;

import java.util.Date;

import org.apache.log4j.Logger;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.PersistJobDataAfterExecution;

/**
 * <p>
 * A dumb implementation of Job, for unit testing purposes.
 * </p>
 * 
 * @author James House
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class StatefulDumbJob implements Job {
	private static Logger logger = Logger.getLogger(ColorJob.class);

	public static final String NUM_EXECUTIONS  = "NumExecutions";
	public static final String EXECUTION_DELAY = "ExecutionDelay";

	public StatefulDumbJob() {
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
		logger.error("---" + context.getJobDetail().getKey() + " executing.[" + new Date() + "]");

		JobDataMap map = context.getJobDetail().getJobDataMap();

		int executeCount = 0;
		
		if (map.containsKey(NUM_EXECUTIONS)) {
			executeCount = map.getInt(NUM_EXECUTIONS);
		}

		executeCount++;

		map.put(NUM_EXECUTIONS, executeCount);

		long delay = 5000l;
		
		if (map.containsKey(EXECUTION_DELAY)) {
			delay = map.getLong(EXECUTION_DELAY);
		}

		try {
			Thread.sleep(delay);
		} catch (Exception ignore) {
			//
		}

		logger.error("  -" + context.getJobDetail().getKey() + " complete (" + executeCount + ").");
	}
}