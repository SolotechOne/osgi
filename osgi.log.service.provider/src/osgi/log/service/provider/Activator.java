package osgi.log.service.provider;

import org.osgi.framework.*;

import osgi.log.service.interfaces.LogReaderService;
import osgi.log.service.interfaces.LogService;

/**
 * Bundle activator which creates and registers a simple implementation of
 * the OSGi log service.
 *
 * Notes:
 *
 *  - At present the Log bundle uses only simple synchronization for writing
 *    and accessing log entries. This has not been tuned to optimize speed
 *    and concurrency. It may be possible to enhance this with something like
 *    the reader/writer locks in the CONCURRENT class library.
 *
 *   - A simple CircularList implementation is used to hold the log. It may be
 *     nice in future to extend this to make it support a standard collection
 *     interface such as List.
 */
public class Activator implements BundleActivator, BundleListener, FrameworkListener, ServiceListener {
    /** Standard reference to this bundle's context area */
    private BundleContext context = null;

    /** Registration object for the LogServiceFactory */
    private ServiceRegistration logReg = null;
    /** Reference to object implementing the LogServiceFactory */
    private LogServiceFactory logServ = null;

    /** Registration object for LogReaderService */
    private ServiceRegistration readerReg = null;
    /** Reference to object implementing the LogReaderService */
    private LogReaderService readerServ = null;

    /** Number of log entries to keep in log list */
    private int logSize = 100;

    /** Threshold level. Log entries above this level will be dropped */
    private int logThreshold = 3;

    /** List of log entries */
    private LogList log;

    /**
     * Standard start method for bundle. Creates the log list and registers
     * the LogServiceFactory and LogReaderService services. Also adds
     * listeners for various OSGi events, which the OSGi specification states
     * that the Log Service should create entries for.
     *
     * @param context   reference to our <tt>BundleContext</tt>
     */
    public void start(BundleContext context) throws Exception {
        this.context = context;

        //TODO: include better config option handling. These just temp.
        //      Use somethinhg like Configurable or ConfigAdmin.
        try {
        	String strProp = context.getProperty("osgi.log.service.provider.log.size");
        	
        	if (strProp != null) {
        		this.logSize = Integer.parseInt(strProp);
        	}
        }
        catch (NumberFormatException e) {
        	// seems like we should log some form of error, which is tricky!
        	this.logSize = 100;
        }

        try {
        	String strProp = context.getProperty("osgi.log.service.provider.log.threshold");
        	
            if (strProp != null) {
                this.logThreshold = Integer.parseInt(strProp);
            }
        }
        catch (NumberFormatException e) {
            // seems like we should log some form of error, which is tricky!
            this.logThreshold = 3;
        }

        // create the log and the log and reader services
        this.log = new LogList(this.logSize, this.logThreshold);
        this.logServ = new LogServiceFactory(this.log);
        this.readerServ = new LogReaderServiceImpl(this.log);

        // register the log and reader services
        this.logReg = this.context.registerService(LogService.class.getName(), this.logServ, null);
        this.readerReg = this.context.registerService(LogReaderService.class.getName(), this.readerServ, null);

        // add the required listeners for framework event logging
//        this.context.addFrameworkListener(this);
//        this.context.addBundleListener(this);
//        this.context.addServiceListener(this);
    }


    /**
     * Standard stop method for bundle. Basically reverses steps taken in start.
     *
     * @param context   reference to our <tt>BundleContext</tt>
     */
    public void stop(BundleContext context) {
        // remove listeners
        this.context.removeFrameworkListener(this);
        this.context.removeBundleListener(this);
        this.context.removeServiceListener(this);

        if (this.logReg != null) {
            this.logReg.unregister();
        }

        if (this.readerReg != null) {
            this.readerReg.unregister();
        }

        if (this.log != null) {
            this.log.shutdown();
        }
    }

    //////////////////////////////////////////////////
    // INTERFACE METHODS - BundleListener
    //////////////////////////////////////////////////

    /**
     * Callback notifying us of a bundle state change. Logs a message
     * conforming to the mapping defined in the OSGi spec.
     *
     * @param event     bundle event which has occured
     */
    public void bundleChanged(BundleEvent event) {
        String msg = null;
        
        switch (event.getType()) {
            case BundleEvent.INSTALLED:
                msg = "BundleEvent.INSTALLED";
                break;

            case BundleEvent.STARTED:
                msg = "BundleEvent.STARTED";
                break;

            case BundleEvent.STOPPED:
                msg = "BundleEvent.STOPPED";
                break;

            case BundleEvent.UNINSTALLED:
                msg = "BundleEvent.UNINSTALLED";
                break;

            case BundleEvent.UPDATED:
                msg = "BundleEvent.UPDATED";
                break;
        }

        this.log.log(event.getBundle(), LogService.LOG_INFO, msg, null, null);
    }

    //////////////////////////////////////////////////
    // INTERFACE METHODS - FrameworkListener
    //////////////////////////////////////////////////

    /**
     * Callback notifying us of a Framework event occurence. Logs a message
     * conforming to the mapping defined in the OSGi spec.
     *
     * @param event     framework event which has occured
     */
    public void frameworkEvent(FrameworkEvent event) {
        switch (event.getType()) {
            case FrameworkEvent.ERROR:
                this.log.log(event.getBundle(), LogService.LOG_ERROR,
                        "FrameworkEvent.ERROR", event.getThrowable(), null);
                break;

            case FrameworkEvent.STARTED:
                this.log.log(event.getBundle(), LogService.LOG_INFO,
                        "FrameworkEvent.STARTED", null, null);
                break;
        }

    }

    //////////////////////////////////////////////////
    // INTERFACE METHODS - ServiceListener
    //////////////////////////////////////////////////

    /**
     * Callback notifying us of a Service event occurence. Logs a message
     * conforming to the mapping defined in the OSGi spec.
     *
     * @param event     service event which has occured
     */
    public void serviceChanged(ServiceEvent event) {
        String msg = null;
        
        int level = LogService.LOG_INFO;
        
        switch (event.getType()) {
            case ServiceEvent.MODIFIED:
                msg = "ServiceEvent.INSTALLED";
                level = LogService.LOG_DEBUG;
                break;

            case ServiceEvent.REGISTERED:
                msg = "ServiceEvent.REGISTERED";
                break;

            case ServiceEvent.UNREGISTERING:
                msg = "ServiceEvent.UNREGISTERING";
                break;
        }

        this.log.log(event.getServiceReference().getBundle(), level, msg, null, event.getServiceReference());
    }
}
