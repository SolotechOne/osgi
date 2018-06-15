package osgi.log.service.provider;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;

import osgi.log.service.interfaces.LogEntry;

/**
 * Simple implementation of the OSGi LogEntry api.
 **/
public class LogEntryImpl implements LogEntry {
    /**
     * Static factory method for creation of log entries.
     *
     * @param bundle    Bundle logging the entry
     * @param level     Level of the entry
     * @param msg       textual message of the entry
     * @param ex        optional exception associated with the entry
     * @param svcRef    optional reference to service which logged the entry
     */
    public static LogEntry createInstance(Bundle bundle, int level, String msg, Throwable ex, ServiceReference svcRef) {
        return new LogEntryImpl(bundle, level, msg, ex, svcRef);
    }

    /** Bundle logging the entry */
    private final Bundle              bundle;
    /** Optional exception associated with the entry */
    private final Throwable           ex;
    /** Level of the entry */
    private final int                 level;
    /** Textual message of the entry */
    private final String              msg;
    /** Optional reference to service which logged the entry */
    private final ServiceReference    svcRef;
    /** Time in milliseconds the entry was logged */
    private final long                time;

    /**
     * Constructor to create a new log entry. Protected to prevent external    
     * instantiation, creation is preferred through static method.
     *
     * @param bundle    Bundle logging the entry
     * @param level     Level of the entry
     * @param msg       textual message of the entry
     * @param ex        optional exception associated with the entry
     * @param svcRef    optional reference to service which logged the entry
     */
    protected LogEntryImpl(Bundle bundle, int level, String msg, Throwable ex, ServiceReference svcRef) {
        this.bundle     = bundle;
        this.ex         = ex;
        this.level      = level;
        this.msg        = msg;
        this.svcRef     = svcRef;
        this.time       = System.currentTimeMillis();
    }

    /**
     * Standard OSGi method to get bundle which logged the entry.
     *
     * @return      logging bundle 
     */
    public Bundle getBundle() {
        return this.bundle;
    }

    /**
     * Standard OSGi method to get any exception logged with the entry.
     *
     * @return  exception, or null if none supplied
     */
    public Throwable getException() {
        return this.ex;
    }

    /**
     * Standard OSGi method to get the level of the log entry.
     *
     * @return  log entry level
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * Standard OSGi method to get the text message of the log entry.
     *
     * @return  log message text
     */
    public String getMessage() {
        return this.msg;
    }

    /**
     * Standard OSGi method to get any service reference for the log entry.
     *
     * @return  service reference which logged the entry, null if none supplied
     */
    public ServiceReference getServiceReference() {
        return this.svcRef;
    }

    /**
     * Standard OSGi method to get time of the log entry.
     *
     * @return  time in milliseconds entry was logged
     */
    public long getTime() {
        return this.time;
    }
}
