package osgi.msg.service.provider;

import java.util.Iterator;

import osgi.msg.service.interfaces.MessageEntry;
import osgi.msg.service.interfaces.MessageListener;
import osgi.msg.service.interfaces.MessageReaderService;

/**
 * Simple implementation of the OSGi LogReaderService api.
 */
public class MessageReaderServiceImpl implements MessageReaderService {
    /** Reference to the list of log entries */
    private MessageList log;

    /**
     * Prevent instantiation except within this package or by subclasses which 
     * know what they're doing.
     */
    protected MessageReaderServiceImpl() {
    }

    /**
     * Constructor to create a log reader service for the specified log list.
     *
     * @param log   log list from which to retrieve log entries.
     */
    public MessageReaderServiceImpl(MessageList log) {
        this.log = log;        
    }

    /**
     * Standard method to add a listener for log events.
     *
     * @param listener      listener to add 
     */
	@Override
	public void addMessageListener(MessageListener listener) {
		log.addListener(listener);
	}

    /**
     * Standard method to remove a listener for log events.
     *
     * @param listener      listener to add 
     */
	@Override
	public void removeMessageListener(MessageListener listener) {
		log.removeListener(listener);
	}

	/**
     * Standard method to get an enumeration for all log events, ordered
     * with the most recent first.
     *
     * @return  log list enumerator
     */
    public Iterator<MessageEntry> getLog() {
        return log.iterator();
    }

//    /**
//     * Inner class to provide a "most recent first" i.e reverse enumeration
//     * of the supplied array.
//     */
//    private static class ReverseEnumerator implements Enumeration {
//        /** Array that is being enumerated */
//        private Object[] a;
//        /** Current position in the array */
//        private int      next;
//
//        /**
//         * Constructor to create a reverse enumeration for the supplied array
//         *
//         * @param a     object array to enumerate
//         */
//        private ReverseEnumerator(Object[] a) {
//            this.a = a;
//            this.next = a.length - 1;
//        }
//
//        /**
//         * Standard enumeration method.
//         *
//         * @return  true if the enumeration has more elements.
//         */
//        public boolean hasMoreElements() {
//            return (next > 0);
//        }
//
//        /**
//         * Standard enumeration method.
//         *
//         * @return  next object in enumeration.
//         */
//        public Object nextElement() {
//            return a[next--];
//        }
//    }
}
