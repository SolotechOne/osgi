package osgi.quartz.scheduler.consumer.jobs;

import java.util.Date;

import org.apache.log4j.Logger;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

public class SimpleJob2 implements Job {
    private static Logger logger = Logger.getLogger(SimpleJob2.class);

    public SimpleJob2() {
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobKey jobKey = context.getJobDetail().getKey();
        
	    logger.info("SimpleJob2 says: " + jobKey + " executing at " + new Date());
    }
}