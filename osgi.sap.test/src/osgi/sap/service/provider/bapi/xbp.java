package osgi.sap.service.provider.bapi;

import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;

public class xbp {
    public static void bapi_xbp_varinfo(JCoDestination destination, String report, String variant) throws JCoException {
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
        
        JCoStructure bapiret = xbp_varinfo.getExportParameterList().getStructure("RETURN");
        
        if (! (bapiret.getString("TYPE").equals("") || bapiret.getString("TYPE").equals("S") || bapiret.getString("TYPE").equals("W")) ) {
            throw new RuntimeException(bapiret.getString("MESSAGE"));
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
    
    public static void bapi_xbp_variant_info_get(JCoDestination destination, String report) throws JCoException {
    	JCoFunction xbp_variant_info_get = destination.getRepository().getFunction("BAPI_XBP_VARIANT_INFO_GET");
    	
        if (xbp_variant_info_get == null)
        	throw new RuntimeException("BAPI_XBP_VARIANT_INFO_GET not found in SAP.");
        
        xbp_variant_info_get.getImportParameterList().setValue("ABAP_PROGRAM_NAME", report);
        xbp_variant_info_get.getImportParameterList().setValue("EXTERNAL_USER_NAME", "AUDIT");
        
//        'A' - variants that are available for batch as well as for dialog will be selected
//        'B' - ‘batch only’ variants will be selected
//        xbp_varinfo.getImportParameterList().setValue("VARIANT_SELECT_OPTION ", "A");
        xbp_variant_info_get.getImportParameterList().setValue("MORE_INFO", "X");
        
        xbp_variant_info_get.getExportParameterList().setActive("RETURN", true);
        
        try {
        	xbp_variant_info_get.execute(destination);
        }
        catch (AbapException exception) {
            System.out.println(exception.toString());
            
            return;
        }
        
        System.out.println("BAPI_XBP_VARIANT_INFO_GET finished:");
        
        JCoStructure bapiret = xbp_variant_info_get.getExportParameterList().getStructure("RETURN");
        
        if (! (bapiret.getString("TYPE").equals("") || bapiret.getString("TYPE").equals("S") || bapiret.getString("TYPE").equals("W")) ) {
            throw new RuntimeException(bapiret.getString("MESSAGE"));
        }
        
        System.out.println();
        
        
//        JCoTable abap_variant_table = xbp_variant_info_get.getTableParameterList().getTable("ABAP_VARIANT_TABLE");
//        
//        for (int i = 0; i < abap_variant_table.getNumRows(); i++) {
//            abap_variant_table.setRow(i);
//            
//           	System.out.println(abap_variant_table.getString("REPORT") + " " + abap_variant_table.getString("VARIANT"));
//        }
        
        
        JCoTable variant_info = xbp_variant_info_get.getTableParameterList().getTable("VARIANT_INFO");
        
        for (int i = 0; i < variant_info.getNumRows(); i++) {
            variant_info.setRow(i);
            
           	System.out.println(variant_info.getString("REPORT") + " " + variant_info.getString("VARIANT")
   				+ " " + variant_info.getString("VTEXT") + " " + variant_info.getString("ENVIRONMNT")
   				+ " " + variant_info.getString("PROTECTED") + " " + variant_info.getString("ENAME")
   				+ " " + variant_info.getString("EDAT") + " " + variant_info.getString("AENAME")
   				+ " " + variant_info.getString("AEDAT") + " " + variant_info.getString("MANDT"));
        }
        
        
        System.out.println();
        System.out.println("Variants selected: " + variant_info.getNumRows());
        System.out.println();
    }
    
    public static String bapi_xbp_job_open(JCoDestination destination, String jobname, String jobclass) throws JCoException {
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
        
        JCoStructure bapiret = xbp_job_open.getExportParameterList().getStructure("RETURN");
        
        if (! (bapiret.getString("TYPE").equals("") || bapiret.getString("TYPE").equals("S") || bapiret.getString("TYPE").equals("W")) ) {
            throw new RuntimeException(bapiret.getString("MESSAGE"));
        }
        
        System.out.println(" Jobcount: " + xbp_job_open.getExportParameterList().getString("JOBCOUNT"));
        System.out.println();
        
        return xbp_job_open.getExportParameterList().getString("JOBCOUNT");
    }
    
    public static void bapi_xbp_job_close(JCoDestination destination, String jobname, String jobcount) throws JCoException {
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
        
        JCoStructure bapiret = xbp_job_close.getExportParameterList().getStructure("RETURN");
        
        if (! (bapiret.getString("TYPE").equals("") || bapiret.getString("TYPE").equals("S") || bapiret.getString("TYPE").equals("W")) ) {
            throw new RuntimeException(bapiret.getString("MESSAGE"));
        }
        
        System.out.println();
    }
    
    public static void bapi_xbp_job_add_abap_step(JCoDestination destination, String jobname, String jobcount, String report, String variant) throws JCoException {
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
        
        JCoStructure bapiret = xbp_job_add_step.getExportParameterList().getStructure("RETURN");
        
        if (! (bapiret.getString("TYPE").equals("") || bapiret.getString("TYPE").equals("S") || bapiret.getString("TYPE").equals("W")) ) {
            throw new RuntimeException(bapiret.getString("MESSAGE"));
        }
        
        System.out.println(" StepNumber: " + xbp_job_add_step.getExportParameterList().getString("STEP_NUMBER"));
        System.out.println();
    }
    
    public static void bapi_xbp_job_start_immediately(JCoDestination destination, String jobname, String jobcount) throws JCoException {
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
        
        JCoStructure bapiret = xbp_job_start_immediately.getExportParameterList().getStructure("RETURN");
        
        if (! (bapiret.getString("TYPE").equals("") || bapiret.getString("TYPE").equals("S") || bapiret.getString("TYPE").equals("W")) ) {
            throw new RuntimeException(bapiret.getString("MESSAGE"));
        }
        
        System.out.println();
    }
    
    public static void bapi_xbp_job_start_asap(JCoDestination destination, String jobname, String jobcount) throws JCoException {
    	JCoFunction xbp_job_start_asap = destination.getRepository().getFunction("BAPI_XBP_JOB_START_ASAP");
    	
        if (xbp_job_start_asap == null)
        	throw new RuntimeException("BAPI_XBP_JOB_START_ASAP not found in SAP.");
        
        xbp_job_start_asap.getImportParameterList().setValue("JOBNAME", jobname);
        xbp_job_start_asap.getImportParameterList().setValue("JOBCOUNT", jobcount);
        xbp_job_start_asap.getImportParameterList().setValue("EXTERNAL_USER_NAME", "AUDIT");
//        xbp_job_start_immediately.getImportParameterList().setValue("TARGET_SERVER", "");
//        xbp_job_start_immediately.getImportParameterList().setValue("TARGET_GROUP", "");
        
        xbp_job_start_asap.getExportParameterList().setActive("RETURN", true);
        
        try {
        	xbp_job_start_asap.execute(destination);
        }
        catch (AbapException exception) {
            System.out.println(exception.toString());
            
            return;
        }
        
        System.out.println("BAPI_XBP_JOB_START_ASAP finished:");
        
        JCoStructure bapiret = xbp_job_start_asap.getExportParameterList().getStructure("RETURN");
        
        if (! (bapiret.getString("TYPE").equals("") || bapiret.getString("TYPE").equals("S") || bapiret.getString("TYPE").equals("W")) ) {
            throw new RuntimeException(bapiret.getString("MESSAGE"));
        }
        
        System.out.println();
    }
    
    public static void bapi_xbp_job_status_get(JCoDestination destination, String jobname, String jobcount) throws JCoException {
    	JCoFunction xbp_job_status_get = destination.getRepository().getFunction("BAPI_XBP_JOB_STATUS_GET");
    	
        if (xbp_job_status_get == null)
        	throw new RuntimeException("BAPI_XBP_JOB_STATUS_GET not found in SAP.");
        
        xbp_job_status_get.getImportParameterList().setValue("JOBNAME", jobname);
        xbp_job_status_get.getImportParameterList().setValue("JOBCOUNT", jobcount);
        xbp_job_status_get.getImportParameterList().setValue("EXTERNAL_USER_NAME", "AUDIT");
        
//        STATUS is the status of a job with the following possible values:
//        'R' - active
//        'I' - intercepted
//        'Y' - ready
//        'P' - scheduled
//        'S' - released
//        'A' - cancelled
//        'F' - finished
        xbp_job_status_get.getExportParameterList().setActive("STATUS", true);
        xbp_job_status_get.getExportParameterList().setActive("RETURN", true);
        
//        HAS_CHILD Specifies whether a job is a child, a parent, both, or neither.
//        'P' – job is parent/has children   
//        'C' – job is child
//        'B' – ('both') job is parent and child
//        ' ' - (blank) job is neither parent nor child
        xbp_job_status_get.getExportParameterList().setActive("HAS_CHILD", true);
        
        try {
        	xbp_job_status_get.execute(destination);
        }
        catch (AbapException exception) {
            System.out.println(exception.toString());
            
            return;
        }
        
        System.out.println("BAPI_XBP_JOB_STATUS_GET finished:");
        
        JCoStructure bapiret = xbp_job_status_get.getExportParameterList().getStructure("RETURN");

//    	System.out.println(bapiret.getString("TYPE") + "|" + bapiret.getString("ID")
//    		+ "|" + bapiret.getString("NUMBER") + "|" + bapiret.getString("MESSAGE")
//    		+ "|" + bapiret.getString("LOG_NO") + "|" + bapiret.getString("LOG_MSG_NO")
//    		+ "|" + bapiret.getString("MESSAGE_V1") + "|" + bapiret.getString("MESSAGE_V2")
//    		+ "|" + bapiret.getString("MESSAGE_V3") + "|" + bapiret.getString("MESSAGE_V4")
//    		+ "|" + bapiret.getString("PARAMETER") + "|" + bapiret.getString("ROW")
//    		+ "|" + bapiret.getString("FIELD") + "|" + bapiret.getString("SYSTEM")
//    	);
        
        if (! (bapiret.getString("TYPE").equals("") || bapiret.getString("TYPE").equals("S") || bapiret.getString("TYPE").equals("W")) ) {
            throw new RuntimeException(bapiret.getString("MESSAGE"));
        }
        
        System.out.println();
        System.out.println("job " + jobname + " state: " + xbp_job_status_get.getExportParameterList().getString("STATUS") + " children: " + xbp_job_status_get.getExportParameterList().getString("HAS_CHILD"));
        System.out.println();
    }
    
    public static void bapi_xbp_job_delete(JCoDestination destination, String jobname, String jobcount) throws JCoException {
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
        
        JCoStructure bapiret = xbp_job_delete.getExportParameterList().getStructure("RETURN");
        
        if (! (bapiret.getString("TYPE").equals("") || bapiret.getString("TYPE").equals("S") || bapiret.getString("TYPE").equals("W")) ) {
            throw new RuntimeException(bapiret.getString("MESSAGE"));
        }
        
        System.out.println();
    }
    
    public static void bapi_xbp_job_select(JCoDestination destination, String jobname) throws JCoException {
    	JCoFunction xbp_job_select = destination.getRepository().getFunction("BAPI_XBP_JOB_SELECT");
    	
        if (xbp_job_select == null)
        	throw new RuntimeException("BAPI_XBP_JOB_SELECT not found in SAP.");
        
        JCoStructure job_select_param = xbp_job_select.getImportParameterList().getStructure("JOB_SELECT_PARAM");
        
        job_select_param.setValue("JOBNAME", jobname);
//        job_select_param.setValue("JOBCOUNT", "");
//        job_select_param.setValue("JOBGROUP", "");
        job_select_param.setValue("USERNAME", "*");
//        job_select_param.setValue("FROM_DATE", "20180911");
//        job_select_param.setValue("FROM_TIME", "");
//        job_select_param.setValue("TO_DATE", "20180913");
//        job_select_param.setValue("TO_TIME", "");
//        job_select_param.setValue("NO_DATE", "");
//        job_select_param.setValue("WITH_PRED", "");
//        job_select_param.setValue("EVENTID", "");
//        job_select_param.setValue("EVENTPARM", "");
//        job_select_param.setValue("PRELIM", "X");
//        job_select_param.setValue("SCHEDUL", "X");
//        job_select_param.setValue("READY", "X");
//        job_select_param.setValue("RUNNING", "X");
//        job_select_param.setValue("FINISHED", "X");
        job_select_param.setValue("ABORTED", "X");
        
        
        xbp_job_select.getImportParameterList().setValue("EXTERNAL_USER_NAME", "AUDIT");
        xbp_job_select.getImportParameterList().setValue("JOB_SELECT_PARAM", job_select_param);
//        xbp_job_select.getImportParameterList().setValue("ABAPNAME", "");
//        xbp_job_select.getImportParameterList().setValue("SUSP", "");
//        xbp_job_select.getImportParameterList().setValue("SYSTEMID", "");
        
//        1. AL (default) returns all child jobs regardless what confirmation they have.
//        2. NG returns only those child jobs that do NOT have general confirmation.
//        3. NC returns only those child jobs that do NOT have any confirmation.
//        xbp_job_select.getImportParameterList().setValue("SELECTION", "AL");
        
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
        
        JCoStructure bapiret = xbp_job_select.getExportParameterList().getStructure("RETURN");
        
        // Meldungstyp: S Success, E Error, W Warning, I Info, A Abort
        if (! (bapiret.getString("TYPE").equals("") || bapiret.getString("TYPE").equals("S") || bapiret.getString("TYPE").equals("W")) ) {
//        	System.out.println(bapiret.getString("TYPE") + "|" + bapiret.getString("ID")
//	    		+ "|" + bapiret.getString("NUMBER") + "|" + bapiret.getString("MESSAGE")
//	    		+ "|" + bapiret.getString("LOG_NO") + "|" + bapiret.getString("LOG_MSG_NO")
//	    		+ "|" + bapiret.getString("MESSAGE_V1") + "|" + bapiret.getString("MESSAGE_V2")
//	    		+ "|" + bapiret.getString("MESSAGE_V3") + "|" + bapiret.getString("MESSAGE_V4")
//	    		+ "|" + bapiret.getString("PARAMETER") + "|" + bapiret.getString("ROW")
//	    		+ "|" + bapiret.getString("FIELD") + "|" + bapiret.getString("SYSTEM")
//        	);
        	
            throw new RuntimeException(bapiret.getString("MESSAGE"));
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
        			+ " " + job_head.getString("SDLDATE") + " " + job_head.getString("SDLTIME")
        			+ " " + job_head.getString("STATUS") + " " + job_head.getString("SDLUNAME"));
        }
        
        System.out.println();
        System.out.println("Jobs selected: " + job_head.getNumRows());
        System.out.println();
    }
    
    public static void bapi_xbp_get_intercepted_jobs(JCoDestination destination) throws JCoException {
    	JCoFunction xbp_get_intercepted_jobs = destination.getRepository().getFunction("BAPI_XBP_GET_INTERCEPTED_JOBS");
    	
        if (xbp_get_intercepted_jobs == null)
        	throw new RuntimeException("BAPI_XBP_GET_INTERCEPTED_JOBS not found in SAP.");
        
        xbp_get_intercepted_jobs.getImportParameterList().setValue("EXTERNAL_USER_NAME", "AUDIT");
        
//        1. ‘AL’ (default) – return all intercepted jobs regardless what confirmation they have.
//        2. ‘NG’ – return only those intercepted jobs that do NOT have general confirmation.
//        3. ‘NS’ – return only those intercepted jobs that were NOT confirmed as intercepted.
//        4. ‘NC’ – return only those intercepted jobs that do NOT have any confirmation.
        xbp_get_intercepted_jobs.getImportParameterList().setValue("SELECTION", "NS");
        
//        xbp_get_intercepted_jobs.getImportParameterList().setValue("CLIENT", "");
        xbp_get_intercepted_jobs.getImportParameterList().setValue("MORE_INFO", "X");
        
        xbp_get_intercepted_jobs.getExportParameterList().setActive("RETURN", true);
        
        try {
        	xbp_get_intercepted_jobs.execute(destination);
        }
        catch (AbapException exception) {
            System.out.println(exception.toString());
            
            return;
        }
        
        System.out.println("BAPI_XBP_GET_INTERCEPTED_JOBS finished:");
        
        JCoStructure bapiret = xbp_get_intercepted_jobs.getExportParameterList().getStructure("RETURN");
        
        if (! (bapiret.getString("TYPE").equals("") || bapiret.getString("TYPE").equals("S") || bapiret.getString("TYPE").equals("W")) ) {
            throw new RuntimeException(bapiret.getString("MESSAGE"));
        }
        
        
//        JCoTable jobinfo = xbp_get_intercepted_jobs.getTableParameterList().getTable("JOBINFO");
//        
//        for (int i = 0; i < jobinfo.getNumRows(); i++) {
//        	jobinfo.setRow(i);
//
//        	System.out.println(jobinfo.getString("JOBCOUNT") + " " + jobinfo.getString("JOBNAME"));
//        }
        
        
        System.out.println();
        
        
        JCoTable jobinfo2 = xbp_get_intercepted_jobs.getTableParameterList().getTable("JOBINFO2");
        
        for (int i = 0; i < jobinfo2.getNumRows(); i++) {
        	jobinfo2.setRow(i);
        	
        	System.out.println(jobinfo2.getString("JOBCOUNT") + " " + jobinfo2.getString("JOBNAME")
        			+ " " + jobinfo2.getString("ICPDATE") + " " + jobinfo2.getString("ICPTIME")
        			+ " " + jobinfo2.getString("SDLUNAME"));
        }
        
        System.out.println();
        System.out.println("Jobs selected: " + jobinfo2.getNumRows());
        System.out.println();
    }
    
    public static void bapi_xbp_special_confirm_job(JCoDestination destination, String jobname, String jobcount) throws JCoException {
    	JCoFunction xbp_special_confirm_job = destination.getRepository().getFunction("BAPI_XBP_SPECIAL_CONFIRM_JOB");
    	
        if (xbp_special_confirm_job == null)
        	throw new RuntimeException("BAPI_XBP_SPECIAL_CONFIRM_JOB not found in SAP.");
        
        JCoTable jobs = xbp_special_confirm_job.getTableParameterList().getTable("JOBS");
        
        jobs.appendRow();
        
        jobs.setValue("JOBNAME", jobname);
        jobs.setValue("JOBCOUNT", jobcount);   
        
        xbp_special_confirm_job.getImportParameterList().setValue("EXTERNAL_USER_NAME", "AUDIT");
        
//        1. Confirmation of intercepted jobs: CONFIRMATION = ’i’.
//        2. Confirmation of child jobs: CONFIRMATION = ’c’.
        xbp_special_confirm_job.getImportParameterList().setValue("CONFIRMATION", "i");
        
        xbp_special_confirm_job.getExportParameterList().setActive("RETURN", true);
        
        try {
        	xbp_special_confirm_job.execute(destination);
        }
        catch (AbapException exception) {
            System.out.println(exception.toString());
            
            return;
        }
        
        System.out.println("BAPI_XBP_SPECIAL_CONFIRM_JOB finished:");
        
        JCoStructure bapiret = xbp_special_confirm_job.getExportParameterList().getStructure("RETURN");
        
        if (! (bapiret.getString("TYPE").equals("") || bapiret.getString("TYPE").equals("S") || bapiret.getString("TYPE").equals("W")) ) {
            throw new RuntimeException(bapiret.getString("MESSAGE"));
        }
        
        System.out.println();
    }
    
    public static void bapi_xbp_event_raise(JCoDestination destination, String eventid, String eventparm) throws JCoException {
    	JCoFunction xbp_event_raise = destination.getRepository().getFunction("BAPI_XBP_EVENT_RAISE");
    	
        if (xbp_event_raise == null)
        	throw new RuntimeException("BAPI_XBP_EVENT_RAISE not found in SAP.");
        
        xbp_event_raise.getImportParameterList().setValue("EXTERNAL_USER_NAME", "AUDIT");
        xbp_event_raise.getImportParameterList().setValue("EVENTID", eventid);
        xbp_event_raise.getImportParameterList().setValue("EVENTPARM", eventparm);
        
        xbp_event_raise.getExportParameterList().setActive("RETURN", true);
        
        try {
        	xbp_event_raise.execute(destination);
        }
        catch (AbapException exception) {
            System.out.println(exception.toString());
            
            return;
        }
        
        System.out.println("BAPI_XBP_EVENT_RAISE finished:");
        
        JCoStructure bapiret = xbp_event_raise.getExportParameterList().getStructure("RETURN");
        
        if (! (bapiret.getString("TYPE").equals("") || bapiret.getString("TYPE").equals("S") || bapiret.getString("TYPE").equals("W")) ) {
            throw new RuntimeException(bapiret.getString("MESSAGE"));
        }
        
        System.out.println();
    }
    
    public static void bapi_xbp_btc_evthistory_get(JCoDestination destination, String id, String state, String action) throws JCoException {
    	JCoFunction xbp_btc_evthistory_get = destination.getRepository().getFunction("BAPI_XBP_BTC_EVTHISTORY_GET");
    	
        if (xbp_btc_evthistory_get == null)
        	throw new RuntimeException("BAPI_XBP_BTC_EVTHISTORY_GET not found in SAP.");
        
        xbp_btc_evthistory_get.getImportParameterList().setValue("EXTERNAL_USER_NAME", "AUDIT");
//        xbp_btc_evthistory_get.getImportParameterList().setValue("FROM_TIMESTAMP_UTC", "JJJJMMTThhmmss");
//        xbp_btc_evthistory_get.getImportParameterList().setValue("TO_TIMESTAMP_UTC", "JJJJMMTThhmmss");
        xbp_btc_evthistory_get.getImportParameterList().setValue("EVENTIDS", id);
        xbp_btc_evthistory_get.getImportParameterList().setValue("SELECT_STATE", state);	// Select-Status A=all, N=new (default), C=confirmed
        xbp_btc_evthistory_get.getImportParameterList().setValue("ACTION", action);			// Operationen an Einträgen: C=confirmation (default), N=do nothing
//        xbp_btc_evthistory_get.getImportParameterList().setValue("GUIDS", "");
        xbp_btc_evthistory_get.getImportParameterList().setValue("PARAMS", "*");
        
        xbp_btc_evthistory_get.getExportParameterList().setActive("RETURN", true);
        
        try {
        	xbp_btc_evthistory_get.execute(destination);
        }
        catch (AbapException exception) {
            System.out.println(exception.toString());
            
            return;
        }
        
        System.out.println("BAPI_XBP_BTC_EVTHISTORY_GET finished:");
        
        JCoStructure bapiret = xbp_btc_evthistory_get.getExportParameterList().getStructure("RETURN");
        
        if (! (bapiret.getString("TYPE").equals("") || bapiret.getString("TYPE").equals("S") || bapiret.getString("TYPE").equals("W")) ) {
            throw new RuntimeException(bapiret.getString("MESSAGE"));
        }
        
        
        JCoTable raised_events = xbp_btc_evthistory_get.getTableParameterList().getTable("RAISED_EVENTS");
        
        for (int i = 0; i < raised_events.getNumRows(); i++) {
        	raised_events.setRow(i);
        	
        	System.out.println(raised_events.getString("EVENTGUID") + " " + raised_events.getString("EVENTID")
        			+ " " + raised_events.getString("EVENTPARM") + " " + raised_events.getString("EVENTSERVER")
        			+ " " + raised_events.getString("EVTTIMESTAMP") + " " + raised_events.getString("EVENTSTATE")
        			+ " " + raised_events.getString("PROCESSSTATE") + " " + raised_events.getString("COUNTOFJOBS"));
        }
        
        System.out.println();
        System.out.println("Events selected: " + raised_events.getNumRows());
        System.out.println();
    }
    
    public static void bapi_xbp_btc_evthist_confirm(JCoDestination destination, String guid) throws JCoException {
    	JCoFunction xbp_btc_evthist_confirm = destination.getRepository().getFunction("BAPI_XBP_BTC_EVTHIST_CONFIRM");
    	
        if (xbp_btc_evthist_confirm == null)
        	throw new RuntimeException("BAPI_XBP_BTC_EVTHIST_CONFIRM not found in SAP.");
        
        JCoTable jobs = xbp_btc_evthist_confirm.getTableParameterList().getTable("EVTHIST_GUIDS");
        
        jobs.appendRow();
        
        jobs.setValue("EVENTGUID", guid);

        
        xbp_btc_evthist_confirm.getImportParameterList().setValue("EXTERNAL_USER_NAME", "AUDIT");
        
        xbp_btc_evthist_confirm.getExportParameterList().setActive("RETURN", true);
        
        try {
        	xbp_btc_evthist_confirm.execute(destination);
        }
        catch (AbapException exception) {
            System.out.println(exception.toString());
            
            return;
        }
        
        System.out.println("BAPI_XBP_BTC_EVTHIST_CONFIRM finished:");
        
        JCoStructure bapiret = xbp_btc_evthist_confirm.getExportParameterList().getStructure("RETURN");
        
        if (! (bapiret.getString("TYPE").equals("") || bapiret.getString("TYPE").equals("S") || bapiret.getString("TYPE").equals("W")) ) {
            throw new RuntimeException(bapiret.getString("MESSAGE"));
        }
        
        System.out.println();
    }
    
    public static void bapi_xbp_btc_statistic_get(JCoDestination destination) throws JCoException {
    	JCoFunction xbp_get_curr_bp_resources = destination.getRepository().getFunction("BAPI_XBP_BTC_STATISTIC_GET");
    	
        if (xbp_get_curr_bp_resources == null)
        	throw new RuntimeException("BAPI_XBP_BTC_STATISTIC_GET not found in SAP.");
        
        xbp_get_curr_bp_resources.getImportParameterList().setValue("I_EXTERNAL_USER_NAME", "AUDIT");
        xbp_get_curr_bp_resources.getImportParameterList().setValue("I_T_JOBLIST", "AUDIT");
        
        xbp_get_curr_bp_resources.getExportParameterList().setActive("LOGHANDLE", true);
        xbp_get_curr_bp_resources.getExportParameterList().setActive("T_STATDATA", true);
        xbp_get_curr_bp_resources.getExportParameterList().setActive("RETURN", true);
        
        try {
        	xbp_get_curr_bp_resources.execute(destination);
        }
        catch (AbapException exception) {
            System.out.println(exception.toString());
            
            return;
        }
        
        System.out.println("BAPI_XBP_BTC_STATISTIC_GET finished:");
        
        JCoStructure bapiret = xbp_get_curr_bp_resources.getExportParameterList().getStructure("RETURN");
        
        if (! (bapiret.getString("TYPE").equals("") || bapiret.getString("TYPE").equals("S") || bapiret.getString("TYPE").equals("W")) ) {
            throw new RuntimeException(bapiret.getString("MESSAGE"));
        }
        
        System.out.println();

        JCoTable resource_info = xbp_get_curr_bp_resources.getTableParameterList().getTable("RESOURCE_INFO");
        
        for (int i = 0; i < resource_info.getNumRows(); i++) {
        	resource_info.setRow(i);
        	
        	System.out.println(resource_info.getString("SERVER") + " " + resource_info.getString("HOST")
        			+ " " + resource_info.getString("BTCWPTOTAL") + " " + resource_info.getString("BTCWPFREE")
        			+ " " + resource_info.getString("BTCWPCLSSA"));
        }
        
        System.out.println();
    }
    
    public static void bapi_xbp_get_curr_bp_resources(JCoDestination destination) throws JCoException {
    	JCoFunction xbp_get_curr_bp_resources = destination.getRepository().getFunction("BAPI_XBP_GET_CURR_BP_RESOURCES");
    	
        if (xbp_get_curr_bp_resources == null)
        	throw new RuntimeException("BAPI_XBP_GET_CURR_BP_RESOURCES not found in SAP.");
        
        xbp_get_curr_bp_resources.getImportParameterList().setValue("EXTERNAL_USER_NAME", "AUDIT");
        
        xbp_get_curr_bp_resources.getExportParameterList().setActive("RETURN", true);
        
        try {
        	xbp_get_curr_bp_resources.execute(destination);
        }
        catch (AbapException exception) {
            System.out.println(exception.toString());
            
            return;
        }
        
        System.out.println("BAPI_XBP_GET_CURR_BP_RESOURCES finished:");
        
        JCoStructure bapiret = xbp_get_curr_bp_resources.getExportParameterList().getStructure("RETURN");
        
        if (! (bapiret.getString("TYPE").equals("") || bapiret.getString("TYPE").equals("S") || bapiret.getString("TYPE").equals("W")) ) {
            throw new RuntimeException(bapiret.getString("MESSAGE"));
        }
        
        System.out.println();

        JCoTable resource_info = xbp_get_curr_bp_resources.getTableParameterList().getTable("RESOURCE_INFO");
        
        for (int i = 0; i < resource_info.getNumRows(); i++) {
        	resource_info.setRow(i);
        	
        	System.out.println(resource_info.getString("SERVER") + " " + resource_info.getString("HOST")
        			+ " " + resource_info.getString("BTCWPTOTAL") + " " + resource_info.getString("BTCWPFREE")
        			+ " " + resource_info.getString("BTCWPCLSSA"));
        }
        
        System.out.println();
    }
}