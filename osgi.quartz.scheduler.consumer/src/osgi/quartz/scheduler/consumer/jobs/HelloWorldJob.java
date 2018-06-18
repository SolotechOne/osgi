package osgi.quartz.scheduler.consumer.jobs;

import java.util.Date;

import org.apache.log4j.Logger;

import org.quartz.Job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class HelloWorldJob implements Job {
	private static Logger logger = Logger.getLogger(SimpleJob3.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("hello world " + new Date());
	}
}