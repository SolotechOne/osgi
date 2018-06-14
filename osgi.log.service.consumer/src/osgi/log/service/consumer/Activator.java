package osgi.log.service.consumer;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import osgi.log.service.interfaces.LogReaderService;
import osgi.log.service.interfaces.LogService;


public class Activator implements BundleActivator {
	@Override
	public void start(BundleContext context) throws Exception {
		ServiceReference ref = context.getServiceReference(LogReaderService.class.getName());
		if (ref != null)
		{
		    LogReaderService reader = (LogReaderService) context.getService(ref);
		    reader.addLogListener(new LogWriter());
		}
		
		ServiceReference logref = context.getServiceReference(LogService.class.getName());
        if (logref != null)
        {
            LogService log = (LogService) context.getService(logref);

            log.log(LogService.LOG_INFO, "fuck");
        }
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
	}
}