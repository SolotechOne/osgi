package osgi.quartz.scheduler.consumer.jobs;

import java.util.Date;

import org.apache.log4j.Logger;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

/**
 * <p>
 * A dumb implementation of Job, for unittesting purposes.
 * </p>
 * 
 * @author James House
 */
public class MessageJob implements Job {
	private static Logger logger = Logger.getLogger(MessageJob.class);
	
    public static final String MESSAGE = "msg";

    /**
     * Quartz requires a public empty constructor so that the
     * scheduler can instantiate the class whenever it needs.
     */
    public MessageJob() {
    }

    /**
     * <p>
     * Called by the <code>{@link org.quartz.Scheduler}</code> when a
     * <code>{@link org.quartz.Trigger}</code> fires that is associated with
     * the <code>Job</code>.
     * </p>
     * 
     * @throws JobExecutionException
     *             if there is an exception while executing the job.
     */
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	// This job simply prints out its job name and the date and time that it is running
        JobKey jobKey = context.getJobDetail().getKey();

        logger.info("MessageJob: " + jobKey + " executing at " + new Date());

        String message = (String) context.getJobDetail().getJobDataMap().get(MESSAGE);

        logger.info("MessageJob: msg: " + message);
    }
}