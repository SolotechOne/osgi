package osgi.log.service.provider;

import java.util.Enumeration;

import osgi.log.service.interfaces.LogListener;
import osgi.log.service.interfaces.LogReaderService;

/**
 * Simple implementation of the OSGi LogReaderService api.
 */
public class LogReaderServiceImpl implements  LogReaderService {
    /** Reference to the list of log entries */
    private LogList log;

    /**
     * Prevent instantiation except within this package or by subclasses which 
     * know what they're doing.
     */
    protected LogReaderServiceImpl() {
    }

    /**
     * Constructor to create a log reader service for the specified log list.
     *
     * @param log   log list from which to retrieve log entries.
     */
    protected LogReaderServiceImpl(LogList log) {
        this.log = log;        
    }

    /**
     * Standard method to add a listener for log events.
     *
     * @param listener      listener to add 
     */
    public void addLogListener(LogListener listener) {
        log.addListener(listener);
    }

    /**
     * Standard method to get an enumeration for all log events, ordered
     * with the most recent first.
     *
     * @return  log list enumerator
     */
    public Enumeration getLog() {
        // create enumerator based on a copy of the log list 
        return new ReverseEnumerator(log.toArray());
    }

    /**
     * Standard method to remove a listener for log events.
     *
     * @param listener      listener to add 
     */
    public void removeLogListener(LogListener listener) {
        log.removeListener(listener);
    }

    /**
     * Inner class to provide a "most recent first" i.e reverse enumeration
     * of the supplied array.
     */
    private static class ReverseEnumerator implements Enumeration {
        /** Array that is being enumerated */
        private Object[] a;
        /** Current position in the array */
        private int      next;

        /**
         * Constructor to create a reverse enumeration for the supplied array
         *
         * @param a     object array to enumerate
         */
        private ReverseEnumerator(Object[] a) {
            this.a = a;
            this.next = a.length - 1;
        }

        /**
         * Standard enumeration method.
         *
         * @return  true if the enumeration has more elements.
         */
        public boolean hasMoreElements() {
            return (next > 0);
        }

        /**
         * Standard enumeration method.
         *
         * @return  next object in enumeration.
         */
        public Object nextElement() {
            return a[next--];
        }
    }
}
