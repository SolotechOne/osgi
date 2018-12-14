package osgi.sap.service.provider.commands;

//STATUS is the status of a job with the following possible values:
//'R' - active
//'I' - intercepted
//'Y' - ready
//'P' - scheduled
//'S' - released
//'A' - cancelled
//'F' - finished
//'X' - actual status cannot be determined

public enum JobStatus {
	R, I, Y, P, S, A, F, X
}