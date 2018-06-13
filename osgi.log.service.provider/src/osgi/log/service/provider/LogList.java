/*
 *                 Common Public License Notice
 * 
 * The contents of this file are subject to the Common Public License
 * Version 0.5 (the "License"). You may not use this file except in
 * compliance with the License. A copy of the License should have been 
 * provided in the release which contained this file. If none was provided,
 * copies are available at http://www.opensource.org/
 *
 * Copyright (c) 2002 SoftSell Business Systems, LLC.
 *
 * Contact: SoftSell Business Systems LLC (info@softsell.com)
 * Contributor(s):
 *
 */
package osgi.log.service.provider;

import java.util.ArrayList;
import java.util.Vector;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;

/**
 * Simple log implementation as a circular list of log entries
 */
public class LogList
        extends
                CircularList
        implements
                Runnable
{
    //////////////////////////////////////////////////
    // STATIC VARIABLES
    //////////////////////////////////////////////////

    /** Needed whenever log list is copied, so created as a static */
    private static final LogListener[] EMPTY_ARR = new LogListener[0];

    //////////////////////////////////////////////////
    // STATIC PUBLIC METHODS
    //////////////////////////////////////////////////

    //////////////////////////////////////////////////
    // STATIC PROTECTED METHODS
    //////////////////////////////////////////////////
    
    //////////////////////////////////////////////////
    // STATIC PRIVATE METHODS
    //////////////////////////////////////////////////

    //////////////////////////////////////////////////
    // INSTANCE VARIABLES
    //////////////////////////////////////////////////
    
    /** List of log listeners */
    private Vector listenerList;

    /** Queue of logged events awaiting dispatch */
    private ArrayList   dispatch;

    /** Flag to indicated whether log is active */
    private boolean stopped;

    /** Dispatch thread for logged events */
    private Thread  thread;

    /** Threshold above which to ignore log events */
    private int threshold;

    //////////////////////////////////////////////////
    // CONSTRUCTORS
    //////////////////////////////////////////////////

    /**
     * Constructs a log list of the specified size and threshold.
     *
     * @param size      required capacity of the list
     * @param threshold level of events to log
     */
    protected LogList(int size, int threshold)
    {
        super(size);

        // create the listener and dispatch lists
        this.listenerList = new Vector();
        this.dispatch = new ArrayList();
        this.threshold = threshold;

        // start the dispatch queue
        this.stopped = false;
        thread = new Thread(this);
        thread.start();
    }

    //////////////////////////////////////////////////
    // ACCESSOR METHODS
    //////////////////////////////////////////////////

    //////////////////////////////////////////////////
    // PUBLIC INSTANCE METHODS
    //////////////////////////////////////////////////

    /**
     * Utility method to actually create and record a LogEntry
     *
     * @param bundle        bundle logging the message.
     * @param level         level of log message.
     * @param message       text of log message.
     * @param ex            exception object to record with log message.
     * @param sr            reference to service logging the message.
     */
    public void log(Bundle bundle, int level, String msg, 
            Throwable ex, ServiceReference sr)
    {
        LogEntry ent = LogEntryImpl.createInstance(bundle, level, msg, ex, sr);
        // ignore if above threshold
        if (level <= this.threshold)
        {
            this.add(ent);
        }
        this.fireLoggedEvent(ent);
    }


    /**
     * Add a listener for log events.
     *
     * @param listener      listener to add 
     */
    public void addListener(LogListener listener)
    {
        this.listenerList.add(listener);
    }


    /**
     * Remove a listener for log events.
     *
     * @param listener      listener to remove
     */
    public void removeListener(LogListener listener)
    {
        this.listenerList.remove(listener);
    }


    /**
     * Shutdown the log, stopping the dispatch thread.
     */
    public void shutdown()
    {
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
    public void run()
    {
        while (true)
        {
            FireRequest fr;
            synchronized(this.dispatch)
            {
                // Wait for a dispatch request or for a shutdown request.
                while ((this.dispatch.size() == 0) && (!this.stopped))
                {
                    // Wait until some signals us for work.
                    try 
                    {
                        this.dispatch.wait();
                    } 
                    catch (InterruptedException ex) 
                    {
                        // some form of error?
                    }
                }

                if (this.stopped)
                {
                    return;
                }

                fr = (FireRequest) this.dispatch.remove(0);
            }

            // Deliver the dispatch request.
            for (int ix = 0; ix < fr.reqListeners.length; ix++)
            {
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
    protected void fireLoggedEvent(LogEntry ent)
    {
        // copy listener list, so only listeners at time event was logged
        // will be notified
        LogListener[] copy = (LogListener[]) listenerList.toArray(EMPTY_ARR);

        if (copy.length > 0)
        {
            FireRequest fr = FireRequest.alloc(copy, ent);
            synchronized(this.dispatch)
            {
                this.dispatch.add(fr);
                this.dispatch.notify();
            }
        }
    }

    //////////////////////////////////////////////////
    // PRIVATE INSTANCE METHODS
    //////////////////////////////////////////////////

    //////////////////////////////////////////////////
    // STATIC INNER CLASSES
    //////////////////////////////////////////////////

    /**
     * Inner class to hold requests for LogEvents needing dispatch to log
     *  listeners
     */
    private static class FireRequest
    {
        //////////////////////////////////////////////////
        // STATIC PRIVATE METHODS
        //////////////////////////////////////////////////

        //Note: could extend to implement cacheing of free req's in here if 
        //      performance proves an issue
        private static FireRequest alloc(LogListener[] listeners, LogEntry ent)
        {
            FireRequest fr = new FireRequest();
            fr.reqListeners = listeners;
            fr.reqEntry = ent;
            return fr;
        }

        //////////////////////////////////////////////////
        // INSTANCE VARIABLES
        //////////////////////////////////////////////////

        public LogListener[] reqListeners;
        public LogEntry reqEntry;

        //////////////////////////////////////////////////
        // PRIVATE INSTANCE METHODS
        //////////////////////////////////////////////////

        private void free()
        {
            this.reset();
        }

        private void reset()
        {
            this.reqListeners = null;
            this.reqEntry = null;
        }
    }

    //////////////////////////////////////////////////
    // NON-STATIC INNER CLASSES
    //////////////////////////////////////////////////

}
