package osgi.sap.service.provider.commands;

import java.io.IOException;
import java.util.Properties;

import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.ext.DestinationDataProvider;

import osgi.sap.service.provider.bapi.xbp.xbp;
import osgi.sap.service.provider.bapi.xmi.xmi;
import osgi.sap.service.provider.util.Util;

@Component(
	property = {
		CommandProcessor.COMMAND_SCOPE + ":String=event",
		CommandProcessor.COMMAND_FUNCTION + ":String=raise",
		CommandProcessor.COMMAND_FUNCTION + ":String=events",
		CommandProcessor.COMMAND_FUNCTION + ":String=confirm",
		CommandProcessor.COMMAND_FUNCTION + ":String=defined"
	},
	service = EventCommands.class
)
public class EventCommands {
	private final static String SAPApplicationServer = "cirk1.de.globusgrp.org";
	private final static String SAPSystemNumber = "00";
	@SuppressWarnings("unused")
	private final static String SAPSystem = "RK1-KON";
	@SuppressWarnings("unused")
	private final static String SAPMessageServer = "ascsrk1.de.globusgrp.org";
	@SuppressWarnings("unused")
	private final static String SAPGroup = "public";
	private final static String SAPClient = "200";
	private final static String SAPUser = "batch-uc4";
	private final static String SAPPassword = "uc4amh15";
	private final static String SAPLanguage = "de";
	
	private static JCoDestination destination;
	
	@Activate
    void activate() throws JCoException {
//        System.out.println("event commands activated");
		
        Properties connectProperties = new Properties();
        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, SAPApplicationServer);
        connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,  SAPSystemNumber);
        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, SAPClient);
        connectProperties.setProperty(DestinationDataProvider.JCO_USER,   SAPUser);
        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, SAPPassword);
        connectProperties.setProperty(DestinationDataProvider.JCO_LANG,   SAPLanguage);
        
        Util.createDestinationDataFile("RK1", connectProperties);
        
    	destination = JCoDestinationManager.getDestination("RK1");

        JCoContext.begin(destination);
	}
	
	@Deactivate
	void deactivate() throws JCoException {
//        System.out.println("event commands deactivated");

        JCoContext.end(destination);
	}
	
    @Descriptor("raise event")
    public void raise(@Descriptor("EventID") String eventid, @Descriptor("Event Parameter") String eventparm) throws IOException, JCoException {
        xmi.bapi_xmi_logon(destination);

        xbp.bapi_xbp_event_raise(destination, eventid, eventparm);
        
        xmi.bapi_xmi_logoff(destination);
    }
    
    @Descriptor("select events")
    public void events(@Parameter(names={"-s","--state"}, absentValue="N") @Descriptor("State") String state, @Parameter(names={"-c","--confirm"}, absentValue="N", presentValue="C") @Descriptor("Confirm") String confirm, @Descriptor("EventID") String id) throws IOException, JCoException {
        xmi.bapi_xmi_logon(destination);

        xbp.bapi_xbp_btc_evthistory_get(destination, id, state, confirm);
        
        xmi.bapi_xmi_logoff(destination);
    }
    
    @Descriptor("confirm events")
    public void confirm(@Descriptor("GUID") String guid) throws IOException, JCoException {
        xmi.bapi_xmi_logon(destination);

        xbp.bapi_xbp_btc_evthist_confirm(destination, guid);
        
        xmi.bapi_xmi_logoff(destination);
   }
    
    @Descriptor("event definitions")
    public void defined(@Parameter(names={"-m","--mask"}, absentValue="%") @Descriptor("Mask") String mask, @Parameter(names={"-f","--from"}, absentValue="0") @Descriptor("From") int from, @Parameter(names={"-t","--to"}, absentValue="0") @Descriptor("To") int to) throws IOException, JCoException {
        xmi.bapi_xmi_logon(destination);

        xbp.bapi_xbp_event_definitions_get(destination, mask, from, to);
        
        xmi.bapi_xmi_logoff(destination);
  }
}