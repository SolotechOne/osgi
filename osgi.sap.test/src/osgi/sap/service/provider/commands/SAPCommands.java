package osgi.sap.service.provider.commands;

import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.ext.DestinationDataProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import osgi.sap.service.provider.bapi.xbp;
import osgi.sap.service.provider.bapi.xmi;

@Component(
	property = {
		CommandProcessor.COMMAND_SCOPE + ":String=sap",
		CommandProcessor.COMMAND_FUNCTION + ":String=info",
		CommandProcessor.COMMAND_FUNCTION + ":String=create",
		CommandProcessor.COMMAND_FUNCTION + ":String=select",
		CommandProcessor.COMMAND_FUNCTION + ":String=intercepted",
		CommandProcessor.COMMAND_FUNCTION + ":String=confirm",
		CommandProcessor.COMMAND_FUNCTION + ":String=raise",
		CommandProcessor.COMMAND_FUNCTION + ":String=events",
		CommandProcessor.COMMAND_FUNCTION + ":String=cevents"
	},
	service = SAPCommands.class
)
public class SAPCommands {
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
	
	@Activate
    void activate() {
//        System.out.println("sap commands activated");
    }
	
	@Deactivate
	void deactivate() {
//        System.out.println("sap commands deactivated");
	}
	
    @Descriptor("get variant info")
    public void info(@Descriptor("Report") String report, @Descriptor("Variant") String variant) throws IOException, JCoException {
//    	System.out.println("connecting...");
    	
        Properties connectProperties = new Properties();
        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, SAPApplicationServer);
        connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,  SAPSystemNumber);
        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, SAPClient);
        connectProperties.setProperty(DestinationDataProvider.JCO_USER,   SAPUser);
        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, SAPPassword);
        connectProperties.setProperty(DestinationDataProvider.JCO_LANG,   SAPLanguage);
        
        createDestinationDataFile("RK1", connectProperties);
        
    	JCoDestination destination = JCoDestinationManager.getDestination("RK1");
    	
        System.out.println("Attributes:");
        System.out.println(destination.getAttributes());
    	
        
        JCoContext.begin(destination);
        
        
    	destination.ping();
    	
    	JCoFunction function = destination.getRepository().getFunction("STFC_CONNECTION");
    	
        if (function == null)
        	throw new RuntimeException("STFC_CONNECTION not found in SAP.");
        
        function.getImportParameterList().setValue("REQUTEXT", "Hello SAP");
        
        try {
            function.execute(destination);
        }
        catch (AbapException exception) {
            System.out.println(exception.toString());
            
            return;
        }
        
        System.out.println("STFC_CONNECTION finished:");
        System.out.println(" Echo: " + function.getExportParameterList().getString("ECHOTEXT"));
        System.out.println(" Response: " + function.getExportParameterList().getString("RESPTEXT"));
        System.out.println();
        
        xmi.bapi_xmi_logon(destination);
        
        xbp.bapi_xbp_varinfo(destination, report, variant);
        
        xmi.bapi_xmi_logoff(destination);
        
        JCoContext.end(destination);
    }
    
    @Descriptor("create abap job")
    public void create(@Descriptor("Jobname") String jobname, @Descriptor("Jobklasse") String jobclass) throws IOException, JCoException {
        Properties connectProperties = new Properties();
        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, SAPApplicationServer);
        connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,  SAPSystemNumber);
        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, SAPClient);
        connectProperties.setProperty(DestinationDataProvider.JCO_USER,   SAPUser);
        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, SAPPassword);
        connectProperties.setProperty(DestinationDataProvider.JCO_LANG,   SAPLanguage);
        
        createDestinationDataFile("RK1", connectProperties);
        
    	JCoDestination destination = JCoDestinationManager.getDestination("RK1");
    	
    	
        
        JCoContext.begin(destination);
        
        xmi.bapi_xmi_logon(destination);
        
        String jobcount = xbp.bapi_xbp_job_open(destination, jobname, jobclass);
        
        xbp.bapi_xbp_job_add_abap_step(destination, jobname, jobcount, "RSPARAM", "");
        
        xbp.bapi_xbp_job_close(destination, jobname, jobcount);
        
        xbp.bapi_xbp_job_start_immediately(destination, jobname, jobcount);

//        bapi_xbp_job_delete(destination, jobname, jobcount);
        
        xmi.bapi_xmi_logoff(destination);
        
        JCoContext.end(destination);
    }
    
    @Descriptor("select abap job")
    public void select(@Descriptor("Report") String report) throws IOException, JCoException {
        Properties connectProperties = new Properties();
        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, SAPApplicationServer);
        connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,  SAPSystemNumber);
        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, SAPClient);
        connectProperties.setProperty(DestinationDataProvider.JCO_USER,   SAPUser);
        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, SAPPassword);
        connectProperties.setProperty(DestinationDataProvider.JCO_LANG,   SAPLanguage);
        
        createDestinationDataFile("RK1", connectProperties);
        
    	JCoDestination destination = JCoDestinationManager.getDestination("RK1");
    	
        JCoContext.begin(destination);
        
        xmi.bapi_xmi_logon(destination);
        
        xbp.bapi_xbp_job_select(destination, report);

        xmi.bapi_xmi_logoff(destination);
        
        JCoContext.end(destination);
    }
    
    @Descriptor("select intercepted abap jobs")
    public void intercepted() throws IOException, JCoException {
        Properties connectProperties = new Properties();
        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, SAPApplicationServer);
        connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,  SAPSystemNumber);
        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, SAPClient);
        connectProperties.setProperty(DestinationDataProvider.JCO_USER,   SAPUser);
        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, SAPPassword);
        connectProperties.setProperty(DestinationDataProvider.JCO_LANG,   SAPLanguage);
        
        createDestinationDataFile("RK1", connectProperties);
        
    	JCoDestination destination = JCoDestinationManager.getDestination("RK1");
    	
        JCoContext.begin(destination);
        
        xmi.bapi_xmi_logon(destination);
        
        xbp.bapi_xbp_get_intercepted_jobs(destination);
        
        xmi.bapi_xmi_logoff(destination);
        
        JCoContext.end(destination);
    }
    
    @Descriptor("confirm special abap jobs")
    public void confirm(@Descriptor("Jobname") String jobname, @Descriptor("Jobcount") String jobcount) throws IOException, JCoException {
        Properties connectProperties = new Properties();
        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, SAPApplicationServer);
        connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,  SAPSystemNumber);
        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, SAPClient);
        connectProperties.setProperty(DestinationDataProvider.JCO_USER,   SAPUser);
        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, SAPPassword);
        connectProperties.setProperty(DestinationDataProvider.JCO_LANG,   SAPLanguage);
        
        createDestinationDataFile("RK1", connectProperties);
        
    	JCoDestination destination = JCoDestinationManager.getDestination("RK1");
    	
        JCoContext.begin(destination);
        
        xmi.bapi_xmi_logon(destination);
        
        xbp.bapi_xbp_special_confirm_job(destination, jobname, jobcount);
        
        xmi.bapi_xmi_logoff(destination);
        
        JCoContext.end(destination);
    }
    
    @Descriptor("raise event")
    public void raise(@Descriptor("EventID") String eventid, @Descriptor("Event Parameter") String eventparm) throws IOException, JCoException {
        Properties connectProperties = new Properties();
        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, SAPApplicationServer);
        connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,  SAPSystemNumber);
        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, SAPClient);
        connectProperties.setProperty(DestinationDataProvider.JCO_USER,   SAPUser);
        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, SAPPassword);
        connectProperties.setProperty(DestinationDataProvider.JCO_LANG,   SAPLanguage);
        
        createDestinationDataFile("RK1", connectProperties);
        
    	JCoDestination destination = JCoDestinationManager.getDestination("RK1");
    	
        JCoContext.begin(destination);
        
        xmi.bapi_xmi_logon(destination);
        
        xbp.bapi_xbp_event_raise(destination, eventid, eventparm);
        
        xmi.bapi_xmi_logoff(destination);
        
        JCoContext.end(destination);
    }
        
    @Descriptor("select events")
    public void events(@Parameter(names={"-s","--state"}, absentValue="N") @Descriptor("State") String state, @Parameter(names={"-c","--confirm"}, absentValue="N", presentValue="C") @Descriptor("Confirm") String action, @Descriptor("EventID") String id) throws IOException, JCoException {
    	System.out.println("state: " + state);
    	System.out.println("action: " + action);
    	System.out.println("event: " + id);
    	
        Properties connectProperties = new Properties();
        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, SAPApplicationServer);
        connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,  SAPSystemNumber);
        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, SAPClient);
        connectProperties.setProperty(DestinationDataProvider.JCO_USER,   SAPUser);
        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, SAPPassword);
        connectProperties.setProperty(DestinationDataProvider.JCO_LANG,   SAPLanguage);
        
        createDestinationDataFile("RK1", connectProperties);
        
    	JCoDestination destination = JCoDestinationManager.getDestination("RK1");
    	
        JCoContext.begin(destination);
        
        xmi.bapi_xmi_logon(destination);
        
        xbp.bapi_xbp_btc_evthistory_get(destination, id, state, action);
        
        xmi.bapi_xmi_logoff(destination);
        
        JCoContext.end(destination);
    }
        
    @Descriptor("confirm events")
    public void cevents(@Descriptor("GUID") String guid) throws IOException, JCoException {
        Properties connectProperties = new Properties();
        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, SAPApplicationServer);
        connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,  SAPSystemNumber);
        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, SAPClient);
        connectProperties.setProperty(DestinationDataProvider.JCO_USER,   SAPUser);
        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, SAPPassword);
        connectProperties.setProperty(DestinationDataProvider.JCO_LANG,   SAPLanguage);
        
        createDestinationDataFile("RK1", connectProperties);
        
    	JCoDestination destination = JCoDestinationManager.getDestination("RK1");
    	
        JCoContext.begin(destination);
        
        xmi.bapi_xmi_logon(destination);
        
        xbp.bapi_xbp_btc_evthist_confirm(destination, guid);
        
        xmi.bapi_xmi_logoff(destination);
        
        JCoContext.end(destination);
    }
        
    static void createDestinationDataFile(String destinationName, Properties connectProperties) {
        File destCfg = new File(destinationName+".jcoDestination");
        
//        System.out.println(destCfg.getAbsolutePath());
        
        try {
            FileOutputStream fos = new FileOutputStream(destCfg, false);
            
            connectProperties.store(fos, "connection properties");
            
            fos.close();
        }
        
        catch (Exception e) {
            throw new RuntimeException("Unable to create the destination files", e);
        }
    }
}