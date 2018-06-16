package osgi.msg.service.provider;

import org.osgi.framework.*;

import osgi.msg.service.interfaces.MessageService;

/**
 * Service factory that will be invoked by the OSGi framework to get LogService  * objects for bundles which request a LogService.
 *
 */
public class MessageServiceFactory implements ServiceFactory {
    /** Reference to the list of log entries */
    private MessageList log;

    /**
     * Prevent instantiation except within this package or by subclasses which 
     * know what they're doing.
     */
    protected MessageServiceFactory() {
    }

    /**
     * Constructor to create a log service factory for the specified log list.
     *
     * @param log   log list to be used to record log entries.
     */
    public MessageServiceFactory(MessageList log) {
        this.log = log;        
    }

    /**
     * Standard get method for service factory. Creates a new 
     * LogService instance for the specified bundle.
     *
     * @param bundle        Bundle that requested the log service
     * @param registration  registration object for the service the log service
     * @return              new LogService instance
     */
    public Object getService(Bundle bundle, ServiceRegistration registration) {
        return new MessageServiceImpl(bundle);        
    }

    /**
     * Standard unget method for service factory. 
     *
     * @param bundle        Bundle that requested the log service
     * @param registration  registration object for the service the log service
     * @param service       service object being released.
     */
    public void ungetService(Bundle bundle, ServiceRegistration registration, Object service) {
        // doesn't seem much we need to do in here
    }

    /**
     * Inner class to provide LogService implementation.
     */
    private class MessageServiceImpl implements MessageService {
        /** Reference to bundle which requested this LogService */
        private Bundle bundle;
    
        /**
         * Constructor to create new LogService. Just needs to record the
         * requesting Bundle for use in later logging requests.
         *
         * @param bundle    bundle which requested this LogService.
         */
        private MessageServiceImpl(Bundle bundle) {
            this.bundle = bundle;
        }

		@Override
		public void log(String system, int level, int number, String text, String insert) {
			log.log(system, insert, number, text, level);
		}
    }
}
