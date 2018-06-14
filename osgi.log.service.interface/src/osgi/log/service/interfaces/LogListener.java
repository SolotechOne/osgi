package osgi.log.service.interfaces;

import java.util.EventListener;

import osgi.log.service.interfaces.LogEntry;
import osgi.log.service.interfaces.LogListener;
import osgi.log.service.interfaces.LogReaderService;

/**
 * Subscribes to <tt>LogEntry</tt> objects from the <tt>LogReaderService</tt>.
 *
 * <p>A <tt>LogListener</tt> object may be registered with the Log Reader Service
 * using the <tt>LogReaderService.addLogListener</tt> method.
 * After the listener is registered, the <tt>logged</tt> method will be called for
 * each <tt>LogEntry</tt> object created. The <tt>LogListener</tt> object
 * may be unregistered by calling the
 * <tt>LogReaderService.removeLogListener</tt> method.
 *
 * @version $Revision: 1.6 $
 * @author Open Services Gateway Initiative
 * @see LogReaderService
 * @see LogEntry
 * @see LogReaderService#addLogListener(LogListener)
 * @see LogReaderService#removeLogListener(LogListener)
 */
public abstract interface LogListener extends EventListener
{
    /**
     * Listener method called for each LogEntry object created.
     *
     * <p>As with all event listeners, this method should return to its caller as soon
     * as possible.
     *
     * @param entry A <tt>LogEntry</tt> object containing log information.
     * @see LogEntry
     */
    public abstract void logged(LogEntry entry);
}