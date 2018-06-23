package osgi.quartz.scheduler.consumer.jobs;

import java.util.Date;

import org.apache.log4j.Logger;

import org.osgi.framework.BundleContext;

import org.osgi.service.component.annotations.Reference;

import org.osgi.util.tracker.ServiceTracker;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

import osgi.msg.service.interfaces.MessageService;

public class MsgJob implements Job {
    private static Logger logger = Logger.getLogger(MsgJob.class);

    @Reference
    private BundleContext bundleContext;
    
    private MessageService messageservice;
    
    public MsgJob() {
    	ServiceTracker messageServiceTracker = new ServiceTracker(bundleContext, MessageService.class.getName(), null);

		messageServiceTracker.open();

		messageservice = (MessageService) messageServiceTracker.getService();

		messageServiceTracker.close();
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobKey jobKey = context.getJobDetail().getKey();
        
	    logger.info("SimpleJob1 says: " + jobKey + " executing at " + new Date());
	    
	    
		if (messageservice != null)
			messageservice.log("aedev", MessageService.MESSAGE_TYPE_INFO, 98273, "Job 'JOBS.API@HELLO$WORLD' ended successfully.", "|I|0098273|JOBS.API@HELLO$WORLD");
		else
			System.out.println("no logging service");
    }
}