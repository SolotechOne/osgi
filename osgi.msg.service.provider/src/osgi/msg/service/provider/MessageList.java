package osgi.msg.service.provider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import osgi.msg.service.interfaces.MessageEntry;
import osgi.msg.service.interfaces.MessageListener;

/**
 * Simple log implementation as a circular list of log entries
 */
public class MessageList implements Runnable {
    /** Needed whenever log list is copied, so created as a static */
    private static final MessageListener[] EMPTY_ARR = new MessageListener[0];
    
    private LinkedList<MessageEntry> list = new LinkedList<MessageEntry>();
    
    /** List of log listeners */
    private Vector<MessageListener> listenerList;

    /** Queue of logged events awaiting dispatch */
    private ArrayList dispatch;

    /** Flag to indicated whether log is active */
    private boolean stopped;

    /** Dispatch thread for logged events */
    private Thread thread;

    /** Threshold above which to ignore log events */
    private int threshold;

    /**
     * Constructs a log list of the specified size and threshold.
     *
     * @param size      required capacity of the list
     * @param threshold level of events to log
     */
    public MessageList(int size, int threshold) {
//        super(size);

        // create the listener and dispatch lists
        this.listenerList = new Vector<MessageListener>();
        this.dispatch = new ArrayList();
        this.threshold = threshold;

        // start the dispatch queue
        this.stopped = false;
        thread = new Thread(this);
        thread.start();
    }

    /**
     * Utility method to actually create and record a LogEntry
     *
     * @param bundle        bundle logging the message.
     * @param level         level of log message.
     * @param message       text of log message.
     * @param ex            exception object to record with log message.
     * @param sr            reference to service logging the message.
     */
    public void log(String system, String insert, int number, String text, int type) {
        MessageEntry entry = new MessageEntryImpl(system, insert, number, text, type);
        
        // ignore if above threshold
        if (type <= this.threshold) {
            this.list.add(entry);
        }
        
//        System.out.println(this.list.size());
        
        this.fireLoggedEvent(entry);
    }

    public Iterator<MessageEntry> iterator() {
    	return this.list.iterator();
    }
    
    /**
     * Add a listener for log events.
     *
     * @param listener      listener to add 
     */
    public void addListener(MessageListener listener) {
        this.listenerList.add(listener);
    }

    /**
     * Remove a listener for log events.
     *
     * @param listener      listener to remove
     */
    public void removeListener(MessageListener listener) {
        this.listenerList.remove(listener);
    }

    /**
     * Shutdown the log, stopping the dispatch thread.
     */
    public void shutdown() {
        this.stopped = true;
        this.thread.interrupt();
    }

    //////////////////////////////////////////////////
    // INTERFACE METHODS - Runnable
    //////////////////////////////////////////////////

    /**
     * Standard run method for the dispatch thread. Responsible for performing
     * all actual notification of logged() events to log listeners.
     */
    public void run() {
        while (true) {
            FireRequest fr;
            
            synchronized(this.dispatch) {
                // Wait for a dispatch request or for a shutdown request.
                while ((this.dispatch.size() == 0) && (!this.stopped)) {
                    // Wait until some signals us for work.
                    try  {
                        this.dispatch.wait();
                    } 
                    catch (InterruptedException ex) {
                        // some form of error?
                    }
                }

                if (this.stopped) {
                    return;
                }

                fr = (FireRequest) this.dispatch.remove(0);
            }

            // Deliver the dispatch request.
            for (int ix = 0; ix < fr.reqListeners.length; ix++) {
                fr.reqListeners[ix].logged(fr.reqEntry);
            }

            fr.free();
        }
    }

    //////////////////////////////////////////////////
    // PROTECTED INSTANCE METHODS
    //////////////////////////////////////////////////

    /**
     * Utility method to fire a logged event. Creates and queues a fire request
     * for the event. This will be picked up and dispatched to the current list 
     * of listeners by the dispath thread.
     *
     * @param ent   entry to notify listeners of
     */
    protected void fireLoggedEvent(MessageEntry ent) {
        // copy listener list, so only listeners at time event was logged
        // will be notified
    	MessageListener[] copy = (MessageListener[]) listenerList.toArray(EMPTY_ARR);

        if (copy.length > 0) {
            FireRequest fr = FireRequest.alloc(copy, ent);
            synchronized(this.dispatch) {
                this.dispatch.add(fr);
                this.dispatch.notify();
            }
        }
    }

    /**
     * Inner class to hold requests for LogEvents needing dispatch to log
     *  listeners
     */
    private static class FireRequest {
        //Note: could extend to implement cacheing of free req's in here if 
        //      performance proves an issue
        private static FireRequest alloc(MessageListener[] listeners, MessageEntry ent) {
            FireRequest fr = new FireRequest();
            fr.reqListeners = listeners;
            fr.reqEntry = ent;
            return fr;
        }

        public MessageListener[] reqListeners;
        public MessageEntry reqEntry;

        //////////////////////////////////////////////////
        // PRIVATE INSTANCE METHODS
        //////////////////////////////////////////////////

        private void free() {
            this.reset();
        }

        private void reset() {
            this.reqListeners = null;
            this.reqEntry = null;
        }
    }
}
