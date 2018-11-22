package osgi.sap.service.provider.commands;

import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;

import java.io.IOException;

import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import osgi.sap.service.provider.bapi.xbp.xbp;
import osgi.sap.service.provider.bapi.xmi.xmi;

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
				CommandProcessor.COMMAND_FUNCTION + ":String=start",
				CommandProcessor.COMMAND_FUNCTION + ":String=start_at",
				CommandProcessor.COMMAND_FUNCTION + ":String=statistics",
				CommandProcessor.COMMAND_FUNCTION + ":String=structure"
		},
		service = JobCommands.class
		)
public class JobCommands {
	private static JCoDestination destination;

	@Activate
	void activate() throws JCoException {
		destination = JCoDestinationManager.getDestination("rk1");

		JCoContext.begin(destination);
	}

	@Deactivate
	void deactivate() throws JCoException {
		JCoContext.end(destination);
	}

	@Descriptor("current state of interception")
	public void check(@Descriptor("Interception") String interception, @Descriptor("Parent/Child") String child) throws JCoException {
		try {
			xmi.bapi_xmi_logon(destination);

			xbp.bapi_xbp_new_func_check(destination, interception, child);
		} finally {
			xmi.bapi_xmi_logoff(destination);
		}
	}

	@Descriptor("current state of interception")
	public void criteria() throws JCoException {
		try {
			xmi.bapi_xmi_logon(destination);

			xbp.bapi_xbp_modify_criteria_table(destination);
		} finally {
			xmi.bapi_xmi_logoff(destination);
		}
	}

	@Descriptor("count abap job")
	public void count(@Descriptor("Jobname") String jobname) throws JCoException {
		try {
			xmi.bapi_xmi_logon(destination);

			xbp.bapi_xbp_job_count(destination, jobname);
		} finally {
			xmi.bapi_xmi_logoff(destination);
		}
	}

	@Descriptor("abort abap job")
	public void abort(@Descriptor("Jobname") String jobname, @Descriptor("Jobcount") String jobcount) throws JCoException {
		try {
			xmi.bapi_xmi_logon(destination);

			xbp.bapi_xbp_job_abort(destination, jobname, jobcount);
		} finally {
			xmi.bapi_xmi_logoff(destination);
		}
	}

	@Descriptor("get variant info")
	public void info(@Descriptor("Report") String report, @Descriptor("Variant") String variant) throws JCoException {
		try {
			xmi.bapi_xmi_logon(destination);

			xbp.bapi_xbp_varinfo(destination, report, variant);
		} finally {
			xmi.bapi_xmi_logoff(destination);
		}


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
	}

	@Descriptor("list variants of report")
	public void variants(@Descriptor("Report") String report) throws JCoException {
		try {
			xmi.bapi_xmi_logon(destination);

			xbp.bapi_xbp_variant_info_get(destination, report);
		} finally {
			xmi.bapi_xmi_logoff(destination);
		}
	}

	@Descriptor("search report")
	public void search(@Descriptor("Report") String report) throws JCoException {
		try {
			xmi.bapi_xmi_logon(destination);

			xbp.bapi_xbp_report_search(destination, report);
		} finally {
			xmi.bapi_xmi_logoff(destination);
		}
	}

	@Descriptor("read content of abap job")
	public void read(@Descriptor("Jobname") String jobname, @Descriptor("Jobcount") String jobcount) throws JCoException {
		try {
			xmi.bapi_xmi_logon(destination);

			xbp.bapi_xbp_job_read(destination, jobname, jobcount);
		} finally {
			xmi.bapi_xmi_logoff(destination);
		}
	}

	@Descriptor("read job definition")
	public void definition(@Descriptor("Jobname") String jobname, @Descriptor("Jobcount") String jobcount) throws JCoException {
		try {
			xmi.bapi_xmi_logon(destination);

			xbp.bapi_xbp_job_definition_get(destination, jobname, jobcount);
		} finally {
			xmi.bapi_xmi_logoff(destination);
		}
	}

	@Descriptor("read job spool")
	public void spool(@Descriptor("Spool") String spool) throws JCoException {
		try {
			xmi.bapi_xmi_logon(destination);

			xbp.bapi_xbp_job_read_single_spool(destination, spool);
		} finally {
			xmi.bapi_xmi_logoff(destination);
		}
	}

	@Descriptor("copy abap job")
	public void copy(@Descriptor("Source Jobname") String source_jobname, @Descriptor("Source Jobcount") String source_jobcount, @Descriptor("Target Jobname") String target_jobname) throws JCoException {
		try {
			xmi.bapi_xmi_logon(destination);

			xbp.bapi_xbp_job_copy(destination, source_jobname, source_jobcount, target_jobname);
		} finally {
			xmi.bapi_xmi_logoff(destination);
		}
	}

	@Descriptor("create abap job")
	public void create(@Descriptor("Jobname") String jobname, @Descriptor("Jobklasse") String jobclass, @Descriptor("Report") String report, @Descriptor("Variant") String variant) throws JCoException {
		try {
			xmi.bapi_xmi_logon(destination);

			String jobcount = xbp.bapi_xbp_job_open(destination, jobname, jobclass);

			xbp.bapi_xbp_job_add_abap_step(destination, jobname, jobcount, report, variant);

			//    		xbp.bapi_xbp_job_header_modify(destination, jobname, jobcount);

			xbp.bapi_xbp_job_close(destination, jobname, jobcount);

			//    		xbp.bapi_xbp_job_start_asap(destination, jobname, jobcount);

			//    		xbp.bapi_xbp_job_delete(destination, jobname, jobcount);
		} finally {
			xmi.bapi_xmi_logoff(destination);
		}
	}

	@Descriptor("deschedule abap job")
	public void deschedule(@Descriptor("Jobname") String jobname, @Descriptor("Jobcount") String jobcount) throws JCoException {
		try {
			xmi.bapi_xmi_logon(destination);

			xbp.bapi_xbp_deschedule_job(destination, jobname, jobcount);
		} finally {
			xmi.bapi_xmi_logoff(destination);
		}
	}

	@Descriptor("read job log")
	public void joblog(@Parameter(names={"-r","--reverse"}, absentValue="B", presentValue="E") @Descriptor("Direction") String direction, @Descriptor("Jobname") String jobname, @Descriptor("Jobcount") String jobcount, @Descriptor("Lines") String lines) throws JCoException {
		try {
			xmi.bapi_xmi_logon(destination);

			xbp.bapi_xbp_job_joblog_read(destination, jobname, jobcount, direction, lines);
		} finally {
			xmi.bapi_xmi_logoff(destination);
		}
	}

	@Descriptor("select abap job")
	public void select(@Descriptor("Jobname") String jobname) throws JCoException {
		try {
			xmi.bapi_xmi_logon(destination);

			xbp.bapi_xbp_job_select(destination, jobname);
		} finally {
			xmi.bapi_xmi_logoff(destination);
		}
	}

	@Descriptor("get job status")
	public void status(@Descriptor("Jobname") String jobname, @Descriptor("Jobcount") String jobcount) throws JCoException {
		try {
			xmi.bapi_xmi_logon(destination);

			xbp.bapi_xbp_job_status_get(destination, jobname, jobcount);
		} finally {
			xmi.bapi_xmi_logoff(destination);
		}
	}

	@Descriptor("select intercepted abap jobs")
	public void intercepted() throws IOException, JCoException {
		xmi.bapi_xmi_logon(destination);

		xbp.bapi_xbp_get_intercepted_jobs(destination);

		xmi.bapi_xmi_logoff(destination);
	}

	@Descriptor("confirm special abap jobs")
	public void confirm(@Descriptor("Jobname") String jobname, @Descriptor("Jobcount") String jobcount) throws JCoException {
		try {
			xmi.bapi_xmi_logon(destination);

			jobcount = String.format("%08d", Integer.parseInt(jobcount));

			xbp.bapi_xbp_special_confirm_job(destination, jobname, jobcount);
		} finally {
			xmi.bapi_xmi_logoff(destination);
		}
	}

	@Descriptor("get batch resources")
	public void resources() throws JCoException {
		try {
			xmi.bapi_xmi_logon(destination);

			xbp.bapi_xbp_get_curr_bp_resources(destination);
		} finally {
			xmi.bapi_xmi_logoff(destination);
		}
	}

	@Descriptor("start job immediately")
	public void start(@Descriptor("Jobname") String jobname, @Descriptor("Jobcount") String jobcount) throws JCoException {
		try {
			xmi.bapi_xmi_logon(destination);

			xbp.bapi_xbp_job_start_immediately(destination, jobname, jobcount);
		} finally {
			xmi.bapi_xmi_logoff(destination);
		}
	}

	@Descriptor("start job at")
	public void start_at(@Descriptor("Jobname") String jobname, @Descriptor("Jobcount") String jobcount) throws JCoException {
		try {
			xmi.bapi_xmi_logon(destination);

			xbp.bapi_xbp_job_header_modify(destination, jobname, jobcount);
		} finally {
			xmi.bapi_xmi_logoff(destination);
		}
	}

	@Descriptor("get job statistics")
	public void statistics(@Descriptor("Jobname") String jobname, @Descriptor("Jobcount") String jobcount) throws JCoException {
		try {
			xmi.bapi_xmi_logon(destination);

			xbp.bapi_xbp_btc_statistic_get(destination, jobname, jobcount);
		} finally {
			xmi.bapi_xmi_logoff(destination);
		}
	}

	@Descriptor("get bapi sturcture")
	public void structure(@Descriptor("Bapiname") String bapi) throws JCoException {
		try {
			xmi.bapi_xmi_logon(destination);

//			xbp.bapi_template(destination, bapi);
			
			xbp.print_function_template(destination, bapi);
		} finally {
			xmi.bapi_xmi_logoff(destination);
		}
	}
}