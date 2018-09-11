package osgi.sap.test.commands;

import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;
import com.sap.conn.jco.ext.DestinationDataProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.Descriptor;

import org.osgi.service.component.annotations.Component;

@Component(
	property = {
		CommandProcessor.COMMAND_SCOPE + ":String=sap",
		CommandProcessor.COMMAND_FUNCTION + ":String=info",
		CommandProcessor.COMMAND_FUNCTION + ":String=create",
		CommandProcessor.COMMAND_FUNCTION + ":String=select"
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
        
        bapi_xmi_logon(destination);
        
        bapi_xbp_varinfo(destination, report, variant);
        
        bapi_xmi_logoff(destination);
        
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
        
        bapi_xmi_logon(destination);
        
        String jobcount = bapi_xbp_job_open(destination, jobname, jobclass);
        
        bapi_xbp_job_add_abap_step(destination, jobname, jobcount, "RSPARAM", "");
        
        bapi_xbp_job_close(destination, jobname, jobcount);
        
        bapi_xbp_job_start_immediately(destination, jobname, jobcount);

//        bapi_xbp_job_delete(destination, jobname, jobcount);
        
        bapi_xmi_logoff(destination);
        
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
        
        bapi_xmi_logon(destination);
        
        bapi_xbp_job_select(destination, report);

        bapi_xmi_logoff(destination);
        
        JCoContext.end(destination);
    }
    
    public void bapi_xmi_logon(JCoDestination destination) throws JCoException {
    	JCoFunction xmi_logon = destination.getRepository().getFunction("BAPI_XMI_LOGON");
    	
        if (xmi_logon == null)
        	throw new RuntimeException("BAPI_XMI_LOGON not found in SAP.");
        
        xmi_logon.getImportParameterList().setValue("EXTCOMPANY", "testcompany");
        xmi_logon.getImportParameterList().setValue("EXTPRODUCT", "testproduct");
        xmi_logon.getImportParameterList().setValue("INTERFACE", "XBP");
        xmi_logon.getImportParameterList().setValue("VERSION", "3.0");
        
        xmi_logon.getExportParameterList().setActive("RETURN", true);
        xmi_logon.getExportParameterList().setActive("SESSIONID", true);
        
        try {
        	xmi_logon.execute(destination);
        }
        catch (AbapException e) {
            System.out.println(e.toString());
            
            return;
        }
        
        System.out.println("BAPI_XMI_LOGON finished:");

        JCoStructure returnStructure = xmi_logon.getExportParameterList().getStructure("RETURN");
        
        if (! (returnStructure.getString("TYPE").equals("") || returnStructure.getString("TYPE").equals("S") || returnStructure.getString("TYPE").equals("W")) ) {
            throw new RuntimeException(returnStructure.getString("MESSAGE"));
        }

        if ( xmi_logon.getExportParameterList().isActive("SESSIONID")) {
            System.out.println(" SessionID: " + xmi_logon.getExportParameterList().getString("SESSIONID"));
        }
        
        System.out.println();
    }
    
    public void bapi_xmi_logoff(JCoDestination destination) throws JCoException {
    	JCoFunction xmi_logoff = destination.getRepository().getFunction("BAPI_XMI_LOGOFF");
    	
        if (xmi_logoff == null)
        	throw new RuntimeException("BAPI_XMI_LOGOFF not found in SAP.");
        
        xmi_logoff.getImportParameterList().setValue("INTERFACE", "XBP");
        
        xmi_logoff.getExportParameterList().setActive("RETURN", true);
        
        try {
        	xmi_logoff.execute(destination);
        }
        catch (AbapException e) {
            System.out.println(e.toString());
            
            return;
        }
        
        System.out.println("BAPI_XMI_LOGOFF finished:");

        JCoStructure returnStructure = xmi_logoff.getExportParameterList().getStructure("RETURN");
        
        if (! (returnStructure.getString("TYPE").equals("") || returnStructure.getString("TYPE").equals("S") || returnStructure.getString("TYPE").equals("W")) ) {
            throw new RuntimeException(returnStructure.getString("MESSAGE"));
        }

        System.out.println();
    }
    
    public void bapi_xbp_varinfo(JCoDestination destination, String report, String variant) throws JCoException {
    	JCoFunction xbp_varinfo = destination.getRepository().getFunction("BAPI_XBP_VARINFO");
    	
        if (xbp_varinfo == null)
        	throw new RuntimeException("BAPI_XBP_VARINFO not found in SAP.");
        
        xbp_varinfo.getImportParameterList().setValue("ABAP_PROGRAM_NAME", report);
        xbp_varinfo.getImportParameterList().setValue("VARIANT", variant);
        xbp_varinfo.getImportParameterList().setValue("EXTERNAL_USER_NAME", "AUDIT");
        
//        xbp_varinfo.getExportParameterList().setActive("VARIANT_INFO", true);

        try {
        	xbp_varinfo.execute(destination);
        }
        catch (AbapException exception) {
            System.out.println(exception.toString());
            
            return;
        }
        
        System.out.println("BAPI_XBP_VARINFO finished:");
        
        JCoStructure returnStructure = xbp_varinfo.getExportParameterList().getStructure("RETURN");
        
        if (! (returnStructure.getString("TYPE").equals("") || returnStructure.getString("TYPE").equals("S") || returnStructure.getString("TYPE").equals("W")) ) {
            throw new RuntimeException(returnStructure.getString("MESSAGE"));
        }
        
        System.out.println();
        
        
        JCoTable variant_info = xbp_varinfo.getTableParameterList().getTable("VARIANT_INFO");
        
//        System.out.println(variant.getNumRows());

        for (int i = 0; i < variant_info.getNumRows(); i++) {
            variant_info.setRow(i);

            if ( i == 0 )
            	System.out.printf("Report: %s  Variante: %s\n\n", variant_info.getString("REPORT"), variant_info.getString("VARIANT"));
            	
            	System.out.printf("%-8.8s %s(%s)\t%s\t%s\t%s\t%s (%s)\n"
            		, variant_info.getString("PNAME")
            		, variant_info.getString("PKIND")
            		, variant_info.getString("POLEN")
            		, variant_info.getString("PSIGN")
            		, variant_info.getString("POPTION")
            		, variant_info.getString("PLOW")
            		, variant_info.getString("PHIGH")
            		, variant_info.getString("PTEXT")
            	);
            	
//            System.out.println(variant.getString("PNAME")+ '\t' + variant.getString("PKIND") + '\t' + variant.getString("POLEN")
//            + '\t' + variant.getString("PTEXT") + '\t' + variant.getString("PSIGN") + '\t' + variant.getString("POPTION")
//            + '\t' + variant.getString("PLOW") + '\t' + variant.getString("PHIGH")
//            );
        }

        System.out.println();
    }
    
    public String bapi_xbp_job_open(JCoDestination destination, String jobname, String jobclass) throws JCoException {
    	JCoFunction xbp_job_open = destination.getRepository().getFunction("BAPI_XBP_JOB_OPEN");
    	
        if (xbp_job_open == null)
        	throw new RuntimeException("BAPI_XBP_JOB_OPEN not found in SAP.");
        
        xbp_job_open.getImportParameterList().setValue("JOBNAME", jobname);
        xbp_job_open.getImportParameterList().setValue("JOBCLASS", jobclass);
        xbp_job_open.getImportParameterList().setValue("EXTERNAL_USER_NAME", "AUDIT");
        
        xbp_job_open.getExportParameterList().setActive("RETURN", true);
        xbp_job_open.getExportParameterList().setActive("JOBCOUNT", true);

        try {
        	xbp_job_open.execute(destination);
        }
        catch (AbapException exception) {
            System.out.println(exception.toString());
            
            return null;
        }
        
        System.out.println("BAPI_XBP_JOB_OPEN finished:");
        
        JCoStructure returnStructure = xbp_job_open.getExportParameterList().getStructure("RETURN");
        
        if (! (returnStructure.getString("TYPE").equals("") || returnStructure.getString("TYPE").equals("S") || returnStructure.getString("TYPE").equals("W")) ) {
            throw new RuntimeException(returnStructure.getString("MESSAGE"));
        }
        
        System.out.println(" Jobcount: " + xbp_job_open.getExportParameterList().getString("JOBCOUNT"));
        System.out.println();
        
        return xbp_job_open.getExportParameterList().getString("JOBCOUNT");
    }
    
    public void bapi_xbp_job_close(JCoDestination destination, String jobname, String jobcount) throws JCoException {
    	JCoFunction xbp_job_close = destination.getRepository().getFunction("BAPI_XBP_JOB_CLOSE");
    	
        if (xbp_job_close == null)
        	throw new RuntimeException("BAPI_XBP_JOB_CLOSE not found in SAP.");
        
        xbp_job_close.getImportParameterList().setValue("JOBNAME", jobname);
        xbp_job_close.getImportParameterList().setValue("JOBCOUNT", jobcount);
        xbp_job_close.getImportParameterList().setValue("EXTERNAL_USER_NAME", "AUDIT");
//        xbp_job_close.getImportParameterList().setValue("TARGET_SERVER", "");
//        xbp_job_close.getImportParameterList().setValue("RECIPIENT_OBJ", "");
//        xbp_job_close.getImportParameterList().setValue("RECIPIENT", "");
        
        xbp_job_close.getExportParameterList().setActive("RETURN", true);

        try {
        	xbp_job_close.execute(destination);
        }
        catch (AbapException exception) {
            System.out.println(exception.toString());
            
            return;
        }
        
        System.out.println("BAPI_XBP_JOB_CLOSE finished:");
        
        JCoStructure returnStructure = xbp_job_close.getExportParameterList().getStructure("RETURN");
        
        if (! (returnStructure.getString("TYPE").equals("") || returnStructure.getString("TYPE").equals("S") || returnStructure.getString("TYPE").equals("W")) ) {
            throw new RuntimeException(returnStructure.getString("MESSAGE"));
        }
        
        System.out.println();
    }
    
    public void bapi_xbp_job_add_abap_step(JCoDestination destination, String jobname, String jobcount, String report, String variant) throws JCoException {
    	JCoFunction xbp_job_add_step = destination.getRepository().getFunction("BAPI_XBP_JOB_ADD_ABAP_STEP");
    	
        if (xbp_job_add_step == null)
        	throw new RuntimeException("BAPI_XBP_JOB_ADD_ABAP_STEP not found in SAP.");
        
        xbp_job_add_step.getImportParameterList().setValue("JOBNAME", jobname);
        xbp_job_add_step.getImportParameterList().setValue("JOBCOUNT", jobcount);
        xbp_job_add_step.getImportParameterList().setValue("EXTERNAL_USER_NAME", "AUDIT");
        xbp_job_add_step.getImportParameterList().setValue("ABAP_PROGRAM_NAME", report);
        xbp_job_add_step.getImportParameterList().setValue("ABAP_VARIANT_NAME", variant);
//        xbp_job_close.getImportParameterList().setValue("SAP_USER_NAME", "");
//        xbp_job_close.getImportParameterList().setValue("LANGUAGE", "");
//        xbp_job_close.getImportParameterList().setValue("PRINT_PARAMETERS", "");
//        xbp_job_close.getImportParameterList().setValue("ARCHIVE_PARAMETERS", "");
//        xbp_job_close.getImportParameterList().setValue("ALLPRIPAR", "");
//        xbp_job_close.getImportParameterList().setValue("ALLARCPAR", "");
//        xbp_job_close.getImportParameterList().setValue("FREE_SELINFO", "");
        
        xbp_job_add_step.getExportParameterList().setActive("STEP_NUMBER", true);
        xbp_job_add_step.getExportParameterList().setActive("RETURN", true);
        xbp_job_add_step.getExportParameterList().setActive("TEMP_VARIANT", true);

        try {
        	xbp_job_add_step.execute(destination);
        }
        catch (AbapException exception) {
            System.out.println(exception.toString());
            
            return;
        }
        
        System.out.println("BAPI_XBP_JOB_ADD_ABAP_STEP finished:");
        
        JCoStructure returnStructure = xbp_job_add_step.getExportParameterList().getStructure("RETURN");
        
        if (! (returnStructure.getString("TYPE").equals("") || returnStructure.getString("TYPE").equals("S") || returnStructure.getString("TYPE").equals("W")) ) {
            throw new RuntimeException(returnStructure.getString("MESSAGE"));
        }
        
        System.out.println(" StepNumber: " + xbp_job_add_step.getExportParameterList().getString("STEP_NUMBER"));
        System.out.println();
    }
    
    public void bapi_xbp_job_start_immediately(JCoDestination destination, String jobname, String jobcount) throws JCoException {
    	JCoFunction xbp_job_start_immediately = destination.getRepository().getFunction("BAPI_XBP_JOB_START_IMMEDIATELY");
    	
        if (xbp_job_start_immediately == null)
        	throw new RuntimeException("BAPI_XBP_JOB_START_IMMEDIATELY not found in SAP.");
        
        xbp_job_start_immediately.getImportParameterList().setValue("JOBNAME", jobname);
        xbp_job_start_immediately.getImportParameterList().setValue("JOBCOUNT", jobcount);
        xbp_job_start_immediately.getImportParameterList().setValue("EXTERNAL_USER_NAME", "AUDIT");
//        xbp_job_start_immediately.getImportParameterList().setValue("TARGET_SERVER", "");
//        xbp_job_start_immediately.getImportParameterList().setValue("TARGET_GROUP", "");
        
        xbp_job_start_immediately.getExportParameterList().setActive("RETURN", true);

        try {
        	xbp_job_start_immediately.execute(destination);
        }
        catch (AbapException exception) {
            System.out.println(exception.toString());
            
            return;
        }
        
        System.out.println("BAPI_XBP_JOB_START_IMMEDIATELY finished:");
        
        JCoStructure returnStructure = xbp_job_start_immediately.getExportParameterList().getStructure("RETURN");
        
        if (! (returnStructure.getString("TYPE").equals("") || returnStructure.getString("TYPE").equals("S") || returnStructure.getString("TYPE").equals("W")) ) {
            throw new RuntimeException(returnStructure.getString("MESSAGE"));
        }
        
        System.out.println();
    }
    
    public void bapi_xbp_job_delete(JCoDestination destination, String jobname, String jobcount) throws JCoException {
    	JCoFunction xbp_job_delete = destination.getRepository().getFunction("BAPI_XBP_JOB_DELETE");
    	
        if (xbp_job_delete == null)
        	throw new RuntimeException("BAPI_XBP_JOB_DELETE not found in SAP.");
        
        xbp_job_delete.getImportParameterList().setValue("JOBNAME", jobname);
        xbp_job_delete.getImportParameterList().setValue("JOBCOUNT", jobcount);
        xbp_job_delete.getImportParameterList().setValue("EXTERNAL_USER_NAME", "AUDIT");
        
        xbp_job_delete.getExportParameterList().setActive("RETURN", true);

        try {
        	xbp_job_delete.execute(destination);
        }
        catch (AbapException exception) {
            System.out.println(exception.toString());
            
            return;
        }
        
        System.out.println("BAPI_XBP_JOB_DELETE finished:");
        
        JCoStructure returnStructure = xbp_job_delete.getExportParameterList().getStructure("RETURN");
        
        if (! (returnStructure.getString("TYPE").equals("") || returnStructure.getString("TYPE").equals("S") || returnStructure.getString("TYPE").equals("W")) ) {
            throw new RuntimeException(returnStructure.getString("MESSAGE"));
        }
        
        System.out.println();
    }
    
    public void bapi_xbp_job_select(JCoDestination destination, String jobname) throws JCoException {
    	JCoFunction xbp_job_select = destination.getRepository().getFunction("BAPI_XBP_JOB_SELECT");
    	
        if (xbp_job_select == null)
        	throw new RuntimeException("BAPI_XBP_JOB_SELECT not found in SAP.");

        JCoStructure job_select_param = xbp_job_select.getImportParameterList().getStructure("JOB_SELECT_PARAM");
        
        job_select_param.setValue("JOBNAME", jobname);
        job_select_param.setValue("USERNAME", "*");

        
        xbp_job_select.getImportParameterList().setValue("EXTERNAL_USER_NAME", "AUDIT");
        xbp_job_select.getImportParameterList().setValue("JOB_SELECT_PARAM", job_select_param);
//        xbp_job_select.getImportParameterList().setValue("ABAPNAME", "");
//        xbp_job_select.getImportParameterList().setValue("SUSP", "");
//        xbp_job_select.getImportParameterList().setValue("SYSTEMID", "");
//        xbp_job_select.getImportParameterList().setValue("SELECTION", "");
        
        xbp_job_select.getExportParameterList().setActive("RETURN", true);
        xbp_job_select.getExportParameterList().setActive("ERROR_CODE", true);

        try {
        	xbp_job_select.execute(destination);
        }
        catch (AbapException exception) {
            System.out.println(exception.toString());
            
            return;
        }
        
        System.out.println("BAPI_XBP_JOB_SELECT finished:");
        
        JCoStructure returnStructure = xbp_job_select.getExportParameterList().getStructure("RETURN");
        
        if (! (returnStructure.getString("TYPE").equals("") || returnStructure.getString("TYPE").equals("S") || returnStructure.getString("TYPE").equals("W")) ) {
            throw new RuntimeException(returnStructure.getString("MESSAGE"));
        }
        
        
//        JCoTable selected_jobs = xbp_job_select.getTableParameterList().getTable("SELECTED_JOBS");
//        
//        for (int i = 0; i < selected_jobs.getNumRows(); i++) {
//        	selected_jobs.setRow(i);
//
//        	System.out.println(selected_jobs.getString("JOBCOUNT") + " " + selected_jobs.getString("JOBNAME"));
//        }
//
//
//        System.out.println();
        
        
        JCoTable job_head = xbp_job_select.getTableParameterList().getTable("JOB_HEAD");
        
        for (int i = 0; i < job_head.getNumRows(); i++) {
        	job_head.setRow(i);

        	System.out.println(job_head.getString("JOBCOUNT") + " " + job_head.getString("JOBNAME")
        			+ " " + job_head.getString("STRTDATE") + " " + job_head.getString("STRTTIME")
        			+ " " + job_head.getString("STATUS") + " " + job_head.getString("JOBNAME"));
        }

        
        System.out.println();
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