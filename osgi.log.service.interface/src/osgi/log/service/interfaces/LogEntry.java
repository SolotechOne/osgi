package osgi.log.service.interfaces;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;

/**
 * Provides methods to access the information contained in an individual Log Service log entry.
 *
 * <p>A <tt>LogEntry</tt> object may be acquired from the <tt>LogReaderService.getLog</tt>
 * method or by registering a <tt>LogListener</tt> object.
 *
 * @version $Revision: 1.9 $
 * @author Open Services Gateway Initiative
 * @see LogReaderService#getLog
 * @see LogListener
 */
public abstract interface LogEntry {
    /**
     * Returns the bundle that created this <tt>LogEntry</tt> object.
     *
     * @return The bundle that created this <tt>LogEntry</tt> object; <tt>null</tt> if
     * no bundle is associated with this <tt>LogEntry</tt> object.
     */
    public abstract Bundle getBundle();

    /**
     * Returns the <tt>ServiceReference</tt> object for the service associated with this
     * <tt>LogEntry</tt> object.
     *
     * @return <tt>ServiceReference</tt> object for the service associated with
     * this <tt>LogEntry</tt> object; <tt>null</tt> if no <tt>ServiceReference</tt> object was provided.
     */
    public abstract ServiceReference getServiceReference();

    /**
     * Returns the severity level of this <tt>LogEntry</tt> object.
     *
     * <p>This is one of the severity levels defined by the <tt>LogService</tt> interface.
     *
     * @return Severity level of this <tt>LogEntry</tt> object.
     *
     * @see LogService#LOG_ERROR
     * @see LogService#LOG_WARNING
     * @see LogService#LOG_INFO
     * @see LogService#LOG_DEBUG
     */
    public abstract int getLevel();

    /**
     * Returns the human readable message associated with this <tt>LogEntry</tt> object.
     *
     * @return <tt>String</tt> containing the message associated with this
     * <tt>LogEntry</tt> object.
     */
    public abstract String getMessage();

    /**
     * Returns the exception object associated with this <tt>LogEntry</tt> object.
     *
     * <p>In some implementations, the returned exception may not be the original
     * exception.  To avoid references to a bundle defined exception class, thus
     * preventing an uninstalled bundle from being garbage collected, the Log
     * Service may return an exception object of an implementation defined
     * Throwable subclass.  The returned object will attempt to provide as much
     * information as possible from the original exception object such as the
     * message and stack trace.
     *
     * @return <tt>Throwable</tt> object of the exception associated with
     * this <tt>LogEntry</tt>; <tt>null</tt> if no exception is associated with this <tt>LogEntry</tt> objcet.
     */
    public abstract Throwable getException();

    /**
     * Returns the value of <tt>currentTimeMillis()</tt>
     * at the time this <tt>LogEntry</tt> object was created.
     *
     * @return The system time in milliseconds when this <tt>LogEntry</tt> object was created.
     * @see "System.currentTimeMillis()"
     */
    public abstract long getTime();
}