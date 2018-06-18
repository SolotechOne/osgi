package osgi.msg.service.provider;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import osgi.msg.service.interfaces.MessageReaderService;
import osgi.msg.service.interfaces.MessageService;

public class Activator implements BundleActivator {
    /** Standard reference to this bundle's context area */
    private BundleContext context = null;

    /** Registration object for the MessageServiceFactory */
    private ServiceRegistration messageReg = null;
    /** Reference to object implementing the MessageServiceFactory */
    private MessageServiceFactory messageServ = null;

    /** Registration object for MessageReaderService */
    private ServiceRegistration messagereaderReg = null;
    /** Reference to object implementing the MessageReaderService */
    private MessageReaderService messagereaderServ = null;

    /** Number of log entries to keep in log list */
    private int logSize = 100;

    /** Threshold level. Log entries above this level will be dropped */
    private int logThreshold = 3;

    /** List of message entries */
    private MessageList message;

    /**
     * Standard start method for bundle. Creates the log list and registers
     * the LogServiceFactory and LogReaderService services. Also adds
     * listeners for various OSGi events, which the OSGi specification states
     * that the Log Service should create entries for.
     *
     * @param context   reference to our <tt>BundleContext</tt>
     */
    public void start(BundleContext context) throws Exception {
        this.context = context;

        //TODO: include better config option handling. These just temp.
        //      Use somethinhg like Configurable or ConfigAdmin.
        try {
        	String strProp = context.getProperty("osgi.log.service.provider.log.size");
        	
        	if (strProp != null) {
        		this.logSize = Integer.parseInt(strProp);
        	}
        }
        catch (NumberFormatException e) {
        	// seems like we should log some form of error, which is tricky!
        	this.logSize = 100;
        }

        try {
        	String strProp = context.getProperty("osgi.log.service.provider.log.threshold");
        	
            if (strProp != null) {
                this.logThreshold = Integer.parseInt(strProp);
            }
        }
        catch (NumberFormatException e) {
            // seems like we should log some form of error, which is tricky!
            this.logThreshold = 3;
        }
        
        // create the log and the log and reader services
        this.message = new MessageList(this.logSize, this.logThreshold);
        this.messageServ = new MessageServiceFactory(this.message);
        this.messagereaderServ = new MessageReaderServiceImpl(this.message);

        // register the log and reader services
        this.messageReg = this.context.registerService(MessageService.class.getName(), this.messageServ, null);
        this.messagereaderReg = this.context.registerService(MessageReaderService.class.getName(), this.messagereaderServ, null);
    }

    /**
     * Standard stop method for bundle. Basically reverses steps taken in start.
     *
     * @param context   reference to our <tt>BundleContext</tt>
     */
    public void stop(BundleContext context) {
        if (this.messageReg != null) {
            this.messageReg.unregister();
        }

        if (this.messagereaderReg != null) {
            this.messagereaderReg.unregister();
        }

        if (this.message != null) {
            this.message.shutdown();
        }
    }
}