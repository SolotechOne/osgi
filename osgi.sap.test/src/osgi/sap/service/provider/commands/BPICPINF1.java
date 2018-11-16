package osgi.sap.service.provider.commands;

// Extended info for intercepted jobs
public class BPICPINF1 {
	private String jobname;		// Background job name
	private String jobcount;	// Job ID
	private String authckman;	// Background client for authorization check
	private String sdluname;	// Initiator of job/step scheduling
	private String icpdate;		// Planned Start Date for Background Job
	private String icptime;		// Planned start time for background Job
	private char par_chld;		// Single-Character Flag
	private String par_name;	// Background job name
	private String par_count;	// Job ID
	private int nr_of_chld;		// Natural number

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

	public String getAuthckman() {
		return authckman;
	}

	public void setAuthckman(String authckman) {
		this.authckman = authckman;
	}

	public String getSdluname() {
		return sdluname;
	}

	public void setSdluname(String sdluname) {
		this.sdluname = sdluname;
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

	public char getPar_chld() {
		return par_chld;
	}

	public void setPar_chld(char par_chld) {
		this.par_chld = par_chld;
	}

	public String getPar_name() {
		return par_name;
	}

	public void setPar_name(String par_name) {
		this.par_name = par_name;
	}

	public String getPar_count() {
		return par_count;
	}

	public void setPar_count(String par_count) {
		this.par_count = par_count;
	}

	public int getNr_of_chld() {
		return nr_of_chld;
	}

	public void setNr_of_chld(int nr_of_chld) {
		this.nr_of_chld = nr_of_chld;
	}

	public BPICPINF1() {
		super();
	}

	public BPICPINF1(String jobname, String jobcount, String authckman, String sdluname, String icpdate, String icptime, char par_chld, String par_name, String par_count, int nr_of_chld) {
		super();
		this.jobname = jobname;
		this.jobcount = jobcount;
		this.authckman = authckman;
		this.sdluname = sdluname;
		this.icpdate = icpdate;
		this.icptime = icptime;
		this.par_chld = par_chld;
		this.par_name = par_name;
		this.par_count = par_count;
		this.nr_of_chld = nr_of_chld;
	}

	@Override
	public String toString() {
		return "BPICPINF1 [jobname=" + jobname + ", jobcount=" + jobcount + ", authckman=" + authckman + ", sdluname="
				+ sdluname + ", icpdate=" + icpdate + ", icptime=" + icptime + ", par_chld=" + par_chld + ", par_name="
				+ par_name + ", par_count=" + par_count + ", nr_of_chld=" + nr_of_chld + "]";
	}
}