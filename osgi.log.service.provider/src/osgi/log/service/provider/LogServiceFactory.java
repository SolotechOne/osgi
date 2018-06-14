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

import org.osgi.framework.*;

import osgi.log.service.interfaces.LogService;


/**
 * Service factory that will be invoked by the OSGi framework to get LogService  * objects for bundles which request a LogService.
 *
 */
public class LogServiceFactory 
        implements 
                ServiceFactory
{
    //////////////////////////////////////////////////
    // STATIC VARIABLES
    //////////////////////////////////////////////////

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

    /** Reference to the list of log entries */
    private LogList log;

    //////////////////////////////////////////////////
    // CONSTRUCTORS
    //////////////////////////////////////////////////

    /**
     * Prevent instantiation except within this package or by subclasses which 
     * know what they're doing.
     */
    protected LogServiceFactory()
    {
    }

    /**
     * Constructor to create a log service factory for the specified log list.
     *
     * @param log   log list to be used to record log entries.
     */
    protected LogServiceFactory(LogList log)
    {
        this.log = log;        
    }

    //////////////////////////////////////////////////
    // ACCESSOR METHODS
    //////////////////////////////////////////////////

    //////////////////////////////////////////////////
    // PUBLIC INSTANCE METHODS
    //////////////////////////////////////////////////

    //////////////////////////////////////////////////
    // INTERFACE METHODS - ServiceFactory
    //////////////////////////////////////////////////

    /**
     * Standard get method for service factory. Creates a new 
     * LogService instance for the specified bundle.
     *
     * @param bundle        Bundle that requested the log service
     * @param registration  registration object for the service the log service
     * @return              new LogService instance
     */
    public Object getService(Bundle bundle, ServiceRegistration registration)
    {
        return new LogServiceImpl(bundle);        
    }

    /**
     * Standard unget method for service factory. 
     *
     * @param bundle        Bundle that requested the log service
     * @param registration  registration object for the service the log service
     * @param service       service object being released.
     */
    public void ungetService(Bundle bundle, 
            ServiceRegistration registration, Object service)
    {
        // doesn't seem much we need to do in here
    }

    //////////////////////////////////////////////////
    // PROTECTED INSTANCE METHODS
    //////////////////////////////////////////////////

    //////////////////////////////////////////////////
    // PRIVATE INSTANCE METHODS
    //////////////////////////////////////////////////

    //////////////////////////////////////////////////
    // STATIC INNER CLASSES
    //////////////////////////////////////////////////

    //////////////////////////////////////////////////
    // NON-STATIC INNER CLASSES
    //////////////////////////////////////////////////

    /**
     * Inner class to provide LogService implementation.
     */
    private class LogServiceImpl
            implements
                    LogService
    {
        //////////////////////////////////////////////////
        // INSTANCE VARIABLES
        //////////////////////////////////////////////////

        /** Reference to bundle which requested this LogService */
        private Bundle bundle;
    
        //////////////////////////////////////////////////
        // CONSTRUCTORS
        //////////////////////////////////////////////////

        /**
         * Constructor to create new LogService. Just needs to record the
         * requesting Bundle for use in later logging requests.
         *
         * @param bundle    bundle which requested this LogService.
         */
        private LogServiceImpl(Bundle bundle)
        {
            this.bundle = bundle;
        }

        //////////////////////////////////////////////////
        // INTERFACE METHODS - LogService
        //////////////////////////////////////////////////

        /**
         * Standard OSGi log method.
         *
         * @param level     level of log message.
         * @param message   text of log message.
         */
        public void log(int level, String message)
        {
            log.log(bundle, level, message, null, null);        
        }

        /**
         * Standard OSGi log method.
         *
         * @param level         level of log message.
         * @param message       text of log message.
         * @param exception     exception object to record with log message.
         */
        public void log(int level, String message, Throwable exception)
        {
            log.log(bundle, level, message, exception, null);        
        }

        /**
         * Standard OSGi log method.
         *
         * @param sr            reference to service logging the message.
         * @param level         level of log message.
         * @param message       text of log message.
         */
        public void log(ServiceReference sr, int level, String message)
        {
            log.log(bundle, level, message, null, sr);        
        }

        /**
         * Standard OSGi log method.
         *
         * @param sr            reference to service logging the message.
         * @param level         level of log message.
         * @param message       text of log message.
         * @param exception     exception object to record with log message.
         */
        public void log(ServiceReference sr, int level, String message, 
                Throwable exception)
        {
            log.log(bundle, level, message, exception, sr);
        }

    }
}
