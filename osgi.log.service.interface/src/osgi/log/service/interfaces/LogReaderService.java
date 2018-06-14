package osgi.log.service.interfaces;

import java.util.Enumeration;

import osgi.log.service.interfaces.LogEntry;
import osgi.log.service.interfaces.LogListener;

/**
 * Provides methods to retrieve <tt>LogEntry</tt> objects from the log.
 * <p>There are two ways to retrieve <tt>LogEntry</tt> objects:
 * <ul>
 * <li>The primary way to retrieve <tt>LogEntry</tt> objects is to register a
 * <tt>LogListener</tt> object whose <tt>LogListener.logged</tt>
 * method will be called for each entry added to the log.
 * <li>To retrieve past <tt>LogEntry</tt> objects, the <tt>getLog</tt> method can
 * be called which will return an <tt>Enumeration</tt> of all <tt>LogEntry</tt> objects in the log.
 *
 * @version $Revision: 1.7 $
 * @author Open Services Gateway Initiative
 * @see LogEntry
 * @see LogListener
 * @see LogListener#logged(LogEntry)
 */
public abstract interface LogReaderService
{

    /**
     * Subscribes to <tt>LogEntry</tt> objects.
     *
     * <p>This method registers a <tt>LogListener</tt> object with the Log Reader Service.
     * The <tt>LogListener.logged(LogEntry)</tt>
     * method will be called for each <tt>LogEntry</tt> object placed into the log.
     *
     * <p>When a bundle which registers a <tt>LogListener</tt> object is stopped
     * or otherwise releases the Log Reader Service, the
     * Log Reader Service must remove all of the bundle's listeners.
     *
     * <p>If this Log Reader Service's list of listeners already contains a
     * listener <tt>l</tt> such that <tt>(l==listener)</tt>, this method does
     * nothing.
     *
     * @param listener A <tt>LogListener</tt> object to register; the
     * <tt>LogListener</tt> object is used to receive <tt>LogEntry</tt> objects.
     * @see LogListener
     * @see LogEntry
     * @see LogListener#logged(LogEntry)
     */
    public abstract void addLogListener(LogListener listener);

    /**
     * Unsubscribes to <tt>LogEntry</tt> objects.
     *
     * <p>This method unregisters a <tt>LogListener</tt> object from the Log Reader Service.
     *
     * <p>If <tt>listener</tt> is not contained in
     * this Log Reader Service's list of listeners,
     * this method does nothing.
     *
     * @param   listener A <tt>LogListener</tt> object to unregister.
     * @see LogListener
     */
    public abstract void removeLogListener(LogListener listener);

    /**
     * Returns an <tt>Enumeration</tt> of all <tt>LogEntry</tt> objects in the log.
     *
     * <p>Each element of the enumeration is a <tt>LogEntry</tt> object, ordered
     * with the most recent entry first. Whether the enumeration is of all <tt>LogEntry</tt>
     * objects since the Log Service was started or some recent past is implementation-specific.
     * Also implementation-specific is whether informational and debug
     * <tt>LogEntry</tt> objects are included in the enumeration.
     */
    public abstract Enumeration getLog();
}