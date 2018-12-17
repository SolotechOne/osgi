package osgi.sap.service.provider.commands;

import java.util.concurrent.ThreadPoolExecutor;

public class MonitorThread implements Runnable {
	private ThreadPoolExecutor executor;
	private int seconds;
	private boolean run = true;

	private int poolsize;
	private int corepoolsize;
	private int activecount;
	private long completedtaskcount;
	private long taskcount;
	private boolean isshutdown;
	private boolean isterminated;

	public MonitorThread(ThreadPoolExecutor executor, int delay) {
		this.executor = executor;
		this.seconds = delay;
	}

	public void shutdown() {
		this.run = false;
	}

	@Override
	public void run() {
		while(run) {
			if(activecount != this.executor.getActiveCount() ||
					completedtaskcount != this.executor.getCompletedTaskCount() ||
					taskcount != this.executor.getTaskCount() ||
					isshutdown != this.executor.isShutdown() ||
					isterminated != this.executor.isTerminated()) {
				System.out.println(
						String.format("[monitor] [%d/%d] Active: %d, Completed: %d, Task: %d, isShutdown: %s, isTerminated: %s",
								this.executor.getPoolSize(),
								this.executor.getCorePoolSize(),
								this.executor.getActiveCount(),
								this.executor.getCompletedTaskCount(),
								this.executor.getTaskCount(),
								this.executor.isShutdown(),
								this.executor.isTerminated()));

				activecount = this.executor.getActiveCount();
				completedtaskcount = this.executor.getCompletedTaskCount();
				taskcount = this.executor.getTaskCount();
				isshutdown = this.executor.isShutdown();
				isterminated = this.executor.isTerminated();
			}
			try {
				Thread.sleep(seconds);
			} catch (InterruptedException exception) {
				exception.printStackTrace();
			}
		}
	}
}