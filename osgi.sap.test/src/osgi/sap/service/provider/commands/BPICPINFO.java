package osgi.sap.service.provider.commands;

// Information about an intercepted job
public class BPICPINFO {
	private String jobname;		// Background job name
	private String jobcount;	// Job ID
	private String icpdate;		// Planned Start Date for Background Job
	private String icptime;		// Planned start time for background Job
	
	public String getJobname() {
		return jobname;
	}

	public void setJobname(String jobname) {
		this.jobname = jobname;
	}

	public String getJobcount() {
		return jobcount;
	}

	public void setJobcount(String jobcount) {
		this.jobcount = jobcount;
	}

	public String getIcpdate() {
		return icpdate;
	}

	public void setIcpdate(String icpdate) {
		this.icpdate = icpdate;
	}

	public String getIcptime() {
		return icptime;
	}

	public void setIcptime(String icptime) {
		this.icptime = icptime;
	}

	public BPICPINFO() {
		super();
	}

	public BPICPINFO(String jobname, String jobcount, String icpdate, String icptime) {
		super();
		this.jobname = jobname;
		this.jobcount = jobcount;
		this.icpdate = icpdate;
		this.icptime = icptime;
	}

	@Override
	public String toString() {
		return "BPICPINFO [jobname=" + jobname + ", jobcount=" + jobcount + ", icpdate=" + icpdate + ", icptime=" + icptime + "]";
	}
}