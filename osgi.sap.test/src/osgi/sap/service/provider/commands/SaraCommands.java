package osgi.sap.service.provider.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.Descriptor;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;

import osgi.sap.service.provider.bapi.xmi.xmi;

@Component(
		property = {
				CommandProcessor.COMMAND_SCOPE + ":String=sara",
				CommandProcessor.COMMAND_FUNCTION + ":String=enqueue"
		},
		service = SaraCommands.class
		)
public class SaraCommands {
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

	@Descriptor("enqueue intercepted jobs")
	public void enqueue(@Descriptor("number of active jobs") int maxjobs) throws IOException, JCoException {
		//        xmi.bapi_xmi_logon(destination);

		Thread t = new Thread(){
			@Override
			public void run(){
				//        		System.out.println("Es gibt ein Leben vor dem Tod.");

				ArrayList<BPICPINFO> output;

				try {
					JCoContext.begin(destination);

					xmi.bapi_xmi_logon(destination);

					output = get_vbrk_jobs(destination);

					ListIterator<BPICPINFO> iterator = output.listIterator(0);

					//	                do {
						//	                	System.out.println(iterator.next());
					//	                } while(iterator.hasNext());

					while (!isInterrupted()) {
						//	        			System.out.println("Und er läuft und er läuft und er läuft");

						int count = get_vbrk_active(destination, "ARV_SD_VBRK_DEL*");
						//	        			System.out.println("jobs active " + count + "/" + maxjobs);

						if(count < maxjobs & iterator.hasNext()){
							for(int i = count; i < maxjobs; i++) {
								BPICPINFO row = iterator.next();

								System.out.println("starting job " + row.getJobname() + " " + row.getJobcount());

								start_job(destination, row.getJobname(), row.getJobcount());
							}
						}

						try {
							System.out.println("waiting 60 seconds...");
							Thread.sleep(60000);
						} catch (InterruptedException e) {
							interrupt();
							System.out.println("Unterbrechung in sleep()");
						}
					}

					//	        		System.out.println("Das Ende naht");

					xmi.bapi_xmi_logoff(destination);

					JCoContext.end(destination);
				} catch (JCoException e) {
					e.printStackTrace();
				}
			}
		};

		t.start();

		try {
			Thread.sleep(86400000);	// 24h
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		t.interrupt();

		//        ArrayList<BPICPINFO> output = get_vbrk_jobs(destination);
		//        
		//        ListIterator<BPICPINFO> iterator = output.listIterator(0);
		//        
		////        do {
		////        	BPICPINFO row = iterator.next();
		////        	System.out.println(row.jobname + " " + row.jobcount);
		////        } while(iterator.hasNext());
		//        
		////        for (ListIterator<BPICPINFO> li = output.listIterator(0); li.hasNext();){
		//////        	System.out.println(li.next());
		////        	
		////        	BPICPINFO row = li.next();
		////        	System.out.println(row.jobname + " " + row.jobcount);
		////        }
		//        
		//        int count = get_vbrk_active(destination, "ARV_SD_VBRK_DEL*");
		//        
		//        System.out.println("jobs active " + count);
		//        
		//        if(count < maxjobs & iterator.hasNext()){
		//        	for(int i = count; i < maxjobs; i++) {
		//        		BPICPINFO row = iterator.next();
		//        		
		//        		System.out.println("starte job " + row.jobname + " " + row.jobcount + " ...");
		//        		
		//        		xbp.bapi_xbp_job_start_immediately(destination, row.jobname, row.jobcount);
		//        	}
		//        }
		//        
		//        xmi.bapi_xmi_logoff(destination);
	}

	public static ArrayList<BPICPINFO> get_vbrk_jobs(JCoDestination destination) throws JCoException {
		ArrayList<BPICPINFO> output = new ArrayList<BPICPINFO>();

		JCoFunction xbp_get_intercepted_jobs = destination.getRepository().getFunction("BAPI_XBP_GET_INTERCEPTED_JOBS");

		if (xbp_get_intercepted_jobs == null)
			throw new RuntimeException("BAPI_XBP_GET_INTERCEPTED_JOBS not found in SAP.");

		xbp_get_intercepted_jobs.getImportParameterList().setValue("EXTERNAL_USER_NAME", "AUDIT");

		//        1. 'AL' (default) – return all intercepted jobs regardless what confirmation they have.
		//        2. 'NG' – return only those intercepted jobs that do NOT have general confirmation.
		//        3. 'NS' – return only those intercepted jobs that were NOT confirmed as intercepted.
		//        4. 'NC' – return only those intercepted jobs that do NOT have any confirmation.
		xbp_get_intercepted_jobs.getImportParameterList().setValue("SELECTION", "AL");

		//        xbp_get_intercepted_jobs.getImportParameterList().setValue("CLIENT", "");
		//        xbp_get_intercepted_jobs.getImportParameterList().setValue("MORE_INFO", "X");

		xbp_get_intercepted_jobs.getExportParameterList().setActive("RETURN", true);

		try {
			xbp_get_intercepted_jobs.execute(destination);
		}
		catch (AbapException exception) {
			System.out.println(exception.toString());

			return output;
		}

		//        System.out.println("BAPI_XBP_GET_INTERCEPTED_JOBS finished:");

		JCoStructure bapiret = xbp_get_intercepted_jobs.getExportParameterList().getStructure("RETURN");

		if (! (bapiret.getString("TYPE").equals("") || bapiret.getString("TYPE").equals("S") || bapiret.getString("TYPE").equals("W")) ) {
			throw new RuntimeException(bapiret.getString("MESSAGE"));
		}


		JCoTable jobinfo = xbp_get_intercepted_jobs.getTableParameterList().getTable("JOBINFO");

		for (int i = 0; i < jobinfo.getNumRows(); i++) {
			jobinfo.setRow(i);

			BPICPINFO row = new BPICPINFO(jobinfo.getString("JOBNAME"), jobinfo.getString("JOBCOUNT"), jobinfo.getString("ICPDATE"), jobinfo.getString("ICPTIME"));
			//        	row.setJobname(jobinfo.getString("JOBNAME"));
			//        	row.setJobcount(jobinfo.getString("JOBCOUNT"));
			//        	row.setIcpdate(jobinfo.getString("ICPDATE"));
			//        	row.setIcptime(jobinfo.getString("ICPTIME"));

			output.add(row);

			//        	System.out.println(jobinfo.getString("JOBCOUNT") + " " + jobinfo.getString("JOBNAME"));
		}


		//        System.out.println();


		//        JCoTable jobinfo2 = xbp_get_intercepted_jobs.getTableParameterList().getTable("JOBINFO2");
		//        
		//        for (int i = 0; i < jobinfo2.getNumRows(); i++) {
		//        	jobinfo2.setRow(i);
		//        	
		//        	System.out.println(jobinfo2.getString("JOBCOUNT") + " " + jobinfo2.getString("JOBNAME")
		//        			+ " " + jobinfo2.getString("ICPDATE") + " " + jobinfo2.getString("ICPTIME")
		//        			+ " " + jobinfo2.getString("SDLUNAME"));
		//        }

		//        System.out.println();
		//        System.out.println("Jobs selected: " + jobinfo2.getNumRows());
		//        System.out.println();

		return output;
	}

	public static int get_vbrk_active(JCoDestination destination, String jobname) throws JCoException {
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
		job_select_param.setValue("RUNNING", "X");
		//        job_select_param.setValue("FINISHED", "X");
		//        job_select_param.setValue("ABORTED", "X");


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

			return 0;
		}

		//        System.out.println("BAPI_XBP_JOB_SELECT finished:");

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

		//        for (int i = 0; i < job_head.getNumRows(); i++) {
		//        	job_head.setRow(i);
		//        	
		//        	System.out.println(job_head.getString("JOBCOUNT") + " " + job_head.getString("JOBNAME")
		//        			+ " " + job_head.getString("SDLDATE") + " " + job_head.getString("SDLTIME")
		//        			+ " " + job_head.getString("STATUS") + " " + job_head.getString("SDLUNAME"));
		//        }

		//        System.out.println();
		//        System.out.println("Jobs selected: " + job_head.getNumRows());
		//        System.out.println();

		return job_head.getNumRows();
	}

	public static void start_job(JCoDestination destination, String jobname, String jobcount) throws JCoException {
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

		//        System.out.println("BAPI_XBP_JOB_START_IMMEDIATELY finished:");

		JCoStructure bapiret = xbp_job_start_immediately.getExportParameterList().getStructure("RETURN");

		if (! (bapiret.getString("TYPE").equals("") || bapiret.getString("TYPE").equals("S") || bapiret.getString("TYPE").equals("W")) ) {
			throw new RuntimeException(bapiret.getString("MESSAGE"));
		}

		//        System.out.println();
	}
}