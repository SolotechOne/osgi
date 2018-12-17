package osgi.sap.service.provider.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ThreadPoolExecutor;

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
	public void enqueue(@Descriptor("Jobname") String jobname, @Descriptor("number of active jobs") int maxjobs) throws IOException, JCoException {
//		ExecutorService executor = Executors.newFixedThreadPool(maxjobs);
		
//		ExecutorService executor = new ThreadPoolExecutor(1, maxjobs, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
	
		
		//RejectedExecutionHandler implementation
        RejectedExecutionHandlerImpl rejectionHandler = new RejectedExecutionHandlerImpl();
        //Get the ThreadFactory implementation to use
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        //creating the ThreadPoolExecutor
        ThreadPoolExecutor executor = new ThreadPoolExecutor(maxjobs, maxjobs, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), threadFactory, rejectionHandler);
        //start the monitoring thread
        MonitorThread monitor = new MonitorThread(executor, 100);
        Thread monitorThread = new Thread(monitor);
        monitorThread.start();
        
        
		ArrayList<BPICPINFO> output;
		
		output = get_intercepted_jobs(destination, jobname);
		
		ListIterator<BPICPINFO> iterator = output.listIterator();
		
		List<Callable<String>> tasks = new LinkedList<Callable<String>>();
		
		List<Future<String>> futures;
		
		while(iterator.hasNext()) {
			BPICPINFO row = iterator.next();
			
			ExecuteABAPJob job = new ExecuteABAPJob(destination, row.getJobname(), row.getJobcount());
			
			tasks.add(job);
			
//			executor.submit(job);
		}
		
		try {
//			futures = executor.invokeAll(tasks, 5, TimeUnit.SECONDS);
			futures = executor.invokeAll(tasks);
			
			for(Future<String> future : futures){
	            String ret = future.get(100, TimeUnit.MILLISECONDS);
	            
	            if (future.isDone())
	                System.out.println(ret + " (done=true)");
	            else
	                System.out.println(ret + " (done=false)");
	        }
		} catch (InterruptedException exception) {
			exception.printStackTrace();
		} catch (ExecutionException exception) {
			exception.printStackTrace();
		} catch (TimeoutException exception) {
			exception.printStackTrace();
		}
		
		executor.shutdown();
		
		try {
			if (!executor.awaitTermination(800, TimeUnit.MILLISECONDS)) {
				executor.shutdownNow();
			} 
		} catch (InterruptedException e) {
			executor.shutdownNow();
		}

		monitor.shutdown();
	}
	
	@Descriptor("enqueue intercepted jobs")
	public void sic_enqueue(@Descriptor("Jobname") String jobname, @Descriptor("number of active jobs") int maxjobs) throws IOException, JCoException {
		//        xmi.bapi_xmi_logon(destination);

		Thread t = new Thread(){
			@Override
			public void run(){
				//        		System.out.println("Es gibt ein Leben vor dem Tod.");

				ArrayList<BPICPINFO> output;

				try {
					JCoContext.begin(destination);

					xmi.bapi_xmi_logon(destination);

					output = get_intercepted_jobs(destination, jobname);

					ListIterator<BPICPINFO> iterator = output.listIterator(0);

					//	                do {
					//	                	System.out.println(iterator.next());
					//	                } while(iterator.hasNext());

					while (!isInterrupted()) {
						//	        			System.out.println("Und er läuft und er läuft und er läuft");

						int count = get_vbrk_active(destination, jobname);
						//	        			System.out.println("jobs active " + count + "/" + maxjobs);

						if(count < maxjobs & iterator.hasNext()) {
							for(int i = count; i < maxjobs; i++) {
								if(iterator.hasNext()) {
									BPICPINFO row = iterator.next();

									System.out.println("starting job " + row.getJobname() + " " + row.getJobcount());

									start_job(destination, row.getJobname(), row.getJobcount());
								}
							}
						}
						else {
							interrupt();
						}

//						Thread.sleep(1000);
						
						try {
//							System.out.println("waiting 10 seconds...");
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							interrupt();

							System.out.println("Unterbrechung in sleep()");
						}
					}

					System.out.println("Das Ende naht");


					try {
						join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					xmi.bapi_xmi_logoff(destination);

					JCoContext.end(destination);
				} catch (JCoException e) {
					e.printStackTrace();
				}
			}
		};

		t.start();

//		try {
//			Thread.sleep(86400000);	// 24h
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//		t.interrupt();

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

	public static ArrayList<BPICPINFO> get_intercepted_jobs(JCoDestination destination, String jobname) throws JCoException {
		xmi.bapi_xmi_logon(destination);
		
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

			if ( jobinfo.getString("JOBNAME").startsWith(jobname)) {
				BPICPINFO row = new BPICPINFO(jobinfo.getString("JOBNAME"), jobinfo.getString("JOBCOUNT"), jobinfo.getString("ICPDATE"), jobinfo.getString("ICPTIME"));
				//        	row.setJobname(jobinfo.getString("JOBNAME"));
				//        	row.setJobcount(jobinfo.getString("JOBCOUNT"));
				//        	row.setIcpdate(jobinfo.getString("ICPDATE"));
				//        	row.setIcptime(jobinfo.getString("ICPTIME"));

				output.add(row);
			}

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

		xmi.bapi_xmi_logoff(destination);

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

//			throw new RuntimeException(bapiret.getString("MESSAGE"));
			
			return 0;
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
	
    public static JobStatus get_job_status(JCoDestination destination, String jobname, String jobcount) throws JCoException {
//    	xmi.bapi_xmi_logon(destination);
    	
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
//        'X' - actual status cannot be determined
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
            throw new RuntimeException(exception.toString());
        }
        
//        System.out.println("BAPI_XBP_JOB_STATUS_GET finished:");
        
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
        
//        System.out.println();
//        System.out.println("job " + jobname + " state: " + xbp_job_status_get.getExportParameterList().getString("STATUS") + " children: " + xbp_job_status_get.getExportParameterList().getString("HAS_CHILD"));
//        System.out.println();
        
//        xmi.bapi_xmi_logoff(destination);
        
        switch( xbp_job_status_get.getExportParameterList().getString("STATUS") ) {
        case "R":
        	return JobStatus.R;
        case "I":
        	return JobStatus.I;
        case "Y":
        	return JobStatus.Y;
        case "P":
        	return JobStatus.P;
        case "S":
        	return JobStatus.S;
        case "A":
        	return JobStatus.A;
        case "F":
        	return JobStatus.F;
        default:
        	return JobStatus.X;
        }
    }
}

class ExecuteABAPJob implements Callable<String> {
	private JCoDestination destination;
	private String jobname;
	private String jobcount;
	
	ExecuteABAPJob(JCoDestination destination, String jobname, String jobcount) {
		this.destination = destination;
		this.jobname = jobname;
		this.jobcount = jobcount;
	}
	
//	@Override
//	public void run() {
//		Random rand = new Random();
//		
//		int sleep = rand.nextInt(10000);
//		
//		try {
//			System.out.println("executing job " + jobname + " " + jobcount + " in thread " + Thread.currentThread() + " sleeping for " + sleep + " sec");
//			
//			Thread.sleep(sleep);
//		} catch (InterruptedException exception) {
//			exception.printStackTrace();
//		}
//	}

	@Override
	public String call() throws Exception {
		long start = new Date().getTime();
		
		Random rand = new Random();
		
		int sleep = rand.nextInt(10000);
		
		JobStatus status = null;
		
		try {
			System.out.println("executing job " + jobname + ":" + jobcount + " in thread " + Thread.currentThread());	//  + " sleeping for " + sleep + " sec"
			
//			synchronized (destination) {
				JCoContext.begin(destination);
				xmi.bapi_xmi_logon(destination);
				
				SaraCommands.start_job(destination, jobname, jobcount);
				
//				JobStatus status = SaraCommands.get_job_status(destination, jobname, jobcount);
				
				while (status != JobStatus.F & status != JobStatus.A) {
//					switch (status) {
//					case
//					R:
//						System.out.println(jobname + " " + jobcount + " " + status);
//					default:
//						break;
//					}
					
					Thread.sleep(100);
					
//					long running = new Date().getTime();
//					System.out.print("job " + jobname + ":" + jobcount + " running for " + (running-start) + " msec\r");
					
					JobStatus new_status = SaraCommands.get_job_status(destination, jobname, jobcount);
					
					if (status != new_status) {
						System.out.println("status of job " + jobname + ":" + jobcount + " changed from " + status + " to " + new_status);
						
						status = new_status;
					}
				}
				
				xmi.bapi_xmi_logoff(destination);
				JCoContext.end(destination);
//			}
			
//			Thread.sleep(sleep);
		} catch (InterruptedException exception) {
			exception.printStackTrace();
		}

//		System.out.println("job " + jobname + " " + jobcount + " in thread " + Thread.currentThread() + " (" + sleep + ") stopped");
		
		long stop = new Date().getTime();
		
		System.out.println("job " + jobname + ":" + jobcount + " ended with status: " + status + " in " + (stop-start) + " msec");
		
		return "job " + jobname + ":" + jobcount + " ended with status: " + status + " in " + (stop-start) + " msec";
	}
}