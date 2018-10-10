package osgi.sap.service.provider.commands;

import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.ext.DestinationDataProvider;

import java.io.IOException;
import java.util.Properties;

import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import osgi.sap.service.provider.bapi.xbp.xbp;
import osgi.sap.service.provider.bapi.xmi.xmi;
import osgi.sap.service.provider.util.Util;

@Component(
	property = {
		CommandProcessor.COMMAND_SCOPE + ":String=job",
		CommandProcessor.COMMAND_FUNCTION + ":String=check",
		CommandProcessor.COMMAND_FUNCTION + ":String=criteria",
		CommandProcessor.COMMAND_FUNCTION + ":String=count",
		CommandProcessor.COMMAND_FUNCTION + ":String=abort",
		CommandProcessor.COMMAND_FUNCTION + ":String=info",
		CommandProcessor.COMMAND_FUNCTION + ":String=variants",
		CommandProcessor.COMMAND_FUNCTION + ":String=search",
		CommandProcessor.COMMAND_FUNCTION + ":String=read",
		CommandProcessor.COMMAND_FUNCTION + ":String=definition",
		CommandProcessor.COMMAND_FUNCTION + ":String=spool",
		CommandProcessor.COMMAND_FUNCTION + ":String=copy",
		CommandProcessor.COMMAND_FUNCTION + ":String=create",
		CommandProcessor.COMMAND_FUNCTION + ":String=deschedule",
		CommandProcessor.COMMAND_FUNCTION + ":String=joblog",
		CommandProcessor.COMMAND_FUNCTION + ":String=select",
		CommandProcessor.COMMAND_FUNCTION + ":String=status",
		CommandProcessor.COMMAND_FUNCTION + ":String=intercepted",
		CommandProcessor.COMMAND_FUNCTION + ":String=confirm",
		CommandProcessor.COMMAND_FUNCTION + ":String=raise",
		CommandProcessor.COMMAND_FUNCTION + ":String=events",
		CommandProcessor.COMMAND_FUNCTION + ":String=cevents",
		CommandProcessor.COMMAND_FUNCTION + ":String=devents",
		CommandProcessor.COMMAND_FUNCTION + ":String=resources",
		CommandProcessor.COMMAND_FUNCTION + ":String=statistics",
		CommandProcessor.COMMAND_FUNCTION + ":String=structure"
	},
	service = JobCommands.class
)
public class JobCommands {
//	private final static String SAPApplicationServer = "cirp1.de.globusgrp.org";
//	private final static String SAPSystemNumber = "21";
//	@SuppressWarnings("unused")
//	private final static String SAPSystem = "RK1-PRD";
//	@SuppressWarnings("unused")
//	private final static String SAPMessageServer = "ascsrp1.de.globusgrp.org";

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
//        System.out.println("job commands activated");
		
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
//        System.out.println("job commands deactivated");

        JCoContext.end(destination);
	}
	
	@Descriptor("current state of interception")
    public void check(@Descriptor("Interception") String interception, @Descriptor("Parent/Child") String child) throws IOException, JCoException {
        xmi.bapi_xmi_logon(destination);

        xbp.bapi_xbp_new_func_check(destination, interception, child);
        
        xmi.bapi_xmi_logoff(destination);
    }
    
    @Descriptor("current state of interception")
    public void criteria() throws IOException, JCoException {
    	xmi.bapi_xmi_logon(destination);

    	xbp.bapi_xbp_modify_criteria_table(destination);

    	xmi.bapi_xmi_logoff(destination);
    }
    
    @Descriptor("count abap job")
    public void count(@Descriptor("Jobname") String jobname) throws IOException, JCoException {
    	xmi.bapi_xmi_logon(destination);

    	xbp.bapi_xbp_job_count(destination, jobname);

    	xmi.bapi_xmi_logoff(destination);
    }
    
    @Descriptor("abort abap job")
    public void abort(@Descriptor("Jobname") String jobname, @Descriptor("Jobcount") String jobcount) throws IOException, JCoException {
    	xmi.bapi_xmi_logon(destination);

    	xbp.bapi_xbp_job_abort(destination, jobname, jobcount);

    	xmi.bapi_xmi_logoff(destination);
    }
    
    @Descriptor("get variant info")
    public void info(@Descriptor("Report") String report, @Descriptor("Variant") String variant) throws IOException, JCoException {
    	xmi.bapi_xmi_logon(destination);

//        System.out.println("Attributes:");
//        System.out.println(destination.getAttributes());
    	
        
//    	destination.ping();
//    	
//    	JCoFunction function = destination.getRepository().getFunction("STFC_CONNECTION");
//    	
//        if (function == null)
//        	throw new RuntimeException("STFC_CONNECTION not found in SAP.");
//        
//        function.getImportParameterList().setValue("REQUTEXT", "Hello SAP");
//        
//        try {
//            function.execute(destination);
//        }
//        catch (AbapException exception) {
//            System.out.println(exception.toString());
//            
//            return;
//        }
//        
//        System.out.println("STFC_CONNECTION finished:");
//        System.out.println(" Echo: " + function.getExportParameterList().getString("ECHOTEXT"));
//        System.out.println(" Response: " + function.getExportParameterList().getString("RESPTEXT"));
//        System.out.println();

    	xbp.bapi_xbp_varinfo(destination, report, variant);

    	xmi.bapi_xmi_logoff(destination);
    }
    
    @Descriptor("list variants of report")
    public void variants(@Descriptor("Report") String report) throws IOException, JCoException {
        xmi.bapi_xmi_logon(destination);

        xbp.bapi_xbp_variant_info_get(destination, report);
        
        xmi.bapi_xmi_logoff(destination);
    }
    
    @Descriptor("search report")
    public void search(@Descriptor("Report") String report) throws IOException, JCoException {
    	xmi.bapi_xmi_logon(destination);

    	xbp.bapi_xbp_report_search(destination, report);
        
        xmi.bapi_xmi_logoff(destination);
    }
    
    @Descriptor("read content of abap job")
    public void read(@Descriptor("Jobname") String jobname, @Descriptor("Jobcount") String jobcount) throws IOException, JCoException {
        xmi.bapi_xmi_logon(destination);

        xbp.bapi_xbp_job_read(destination, jobname, jobcount);
        
        xmi.bapi_xmi_logoff(destination);
    }
    
    @Descriptor("read job definition")
	public void definition(@Descriptor("Jobname") String jobname, @Descriptor("Jobcount") String jobcount) throws IOException, JCoException {
		xmi.bapi_xmi_logon(destination);
	
		xbp.bapi_xbp_job_definition_get(destination, jobname, jobcount);
	
		xmi.bapi_xmi_logoff(destination);
	}

    @Descriptor("read job spool")
    public void spool(@Descriptor("Spool") String spool) throws IOException, JCoException {
    	xmi.bapi_xmi_logon(destination);

    	xbp.bapi_xbp_job_read_single_spool(destination, spool);

    	xmi.bapi_xmi_logoff(destination);
    }
    
	@Descriptor("copy abap job")
    public void copy(@Descriptor("Source Jobname") String source_jobname, @Descriptor("Source Jobcount") String source_jobcount, @Descriptor("Target Jobname") String target_jobname) throws IOException, JCoException {
        xmi.bapi_xmi_logon(destination);

        xbp.bapi_xbp_job_copy(destination, source_jobname, source_jobcount, target_jobname);
        
        xmi.bapi_xmi_logoff(destination);
    }
    
    @Descriptor("create abap job")
    public void create(@Descriptor("Jobname") String jobname, @Descriptor("Jobklasse") String jobclass, @Descriptor("Report") String report, @Descriptor("Variant") String variant) throws IOException, JCoException {
        xmi.bapi_xmi_logon(destination);

        String jobcount = xbp.bapi_xbp_job_open(destination, jobname, jobclass);
        
        xbp.bapi_xbp_job_add_abap_step(destination, jobname, jobcount, report, variant);
        
//        xbp.bapi_xbp_job_header_modify(destination, jobname, jobcount);
        
        xbp.bapi_xbp_job_close(destination, jobname, jobcount);
        
//        xbp.bapi_xbp_job_start_asap(destination, jobname, jobcount);

//        bapi_xbp_job_delete(destination, jobname, jobcount);
        
        xmi.bapi_xmi_logoff(destination);
    }
    
    @Descriptor("deschedule abap job")
    public void deschedule(@Descriptor("Jobname") String jobname, @Descriptor("Jobcount") String jobcount) throws IOException, JCoException {
        xmi.bapi_xmi_logon(destination);

        xbp.bapi_xbp_deschedule_job(destination, jobname, jobcount);
        
        xmi.bapi_xmi_logoff(destination);
    }
    
    @Descriptor("read job log")
    public void joblog(@Parameter(names={"-r","--reverse"}, absentValue="B", presentValue="E") @Descriptor("Direction") String direction, @Descriptor("Jobname") String jobname, @Descriptor("Jobcount") String jobcount, @Descriptor("Lines") String lines) throws IOException, JCoException {
        xmi.bapi_xmi_logon(destination);

        xbp.bapi_xbp_job_joblog_read(destination, jobname, jobcount, direction, lines);
        
        xmi.bapi_xmi_logoff(destination);
    }
    
    @Descriptor("select abap job")
    public void select(@Descriptor("Jobname") String jobname) throws IOException, JCoException {
        xmi.bapi_xmi_logon(destination);

        xbp.bapi_xbp_job_select(destination, jobname);
        
        xmi.bapi_xmi_logoff(destination);
    }
    
    @Descriptor("get job status")
    public void status(@Descriptor("Jobname") String jobname, @Descriptor("Jobcount") String jobcount) throws IOException, JCoException {
        xmi.bapi_xmi_logon(destination);

        xbp.bapi_xbp_job_status_get(destination, jobname, jobcount);
        
        xmi.bapi_xmi_logoff(destination);
    }
    
    @Descriptor("select intercepted abap jobs")
    public void intercepted() throws IOException, JCoException {
        xmi.bapi_xmi_logon(destination);

        xbp.bapi_xbp_get_intercepted_jobs(destination);
        
        xmi.bapi_xmi_logoff(destination);
    }
    
    @Descriptor("confirm special abap jobs")
    public void confirm(@Descriptor("Jobname") String jobname, @Descriptor("Jobcount") String jobcount) throws IOException, JCoException {
        xmi.bapi_xmi_logon(destination);

    	jobcount = String.format("%08d", Integer.parseInt(jobcount));
    	
        xbp.bapi_xbp_special_confirm_job(destination, jobname, jobcount);
        
        xmi.bapi_xmi_logoff(destination);
    }
    
    @Descriptor("get batch resources")
    public void resources() throws IOException, JCoException {
        xmi.bapi_xmi_logon(destination);

        xbp.bapi_xbp_get_curr_bp_resources(destination);
        
        xmi.bapi_xmi_logoff(destination);
    }
    
    @Descriptor("get job statistics")
    public void statistics(@Descriptor("Jobname") String jobname, @Descriptor("Jobcount") String jobcount) throws IOException, JCoException {
        xmi.bapi_xmi_logon(destination);

        xbp.bapi_xbp_btc_statistic_get(destination, jobname, jobcount);
        
        xmi.bapi_xmi_logoff(destination);
    }
    
    @Descriptor("get bapi sturcture")
    public void structure(@Descriptor("Bapiname") String bapi) throws IOException, JCoException {
        xmi.bapi_xmi_logon(destination);

        xbp.bapi_template(destination, bapi);
        
        xmi.bapi_xmi_logoff(destination);
    }
}