/* 
 * All content copyright Terracotta, Inc., unless otherwise indicated. All rights reserved. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License.
 * 
 */

package osgi.quartz.scheduler.consumer.examples;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerMetaData;
import org.quartz.impl.StdSchedulerFactory;
import org.apache.log4j.Logger;
import org.osgi.framework.BundleContext;

public class PlugInExample extends Thread {
	private Logger logger = Logger.getLogger(PlugInExample.class);

	private BundleContext context;

	public BundleContext getContext() {
		return context;
	}
	
	private Scheduler scheduler;

	@SuppressWarnings("unused")
	private volatile boolean active = true;

	public PlugInExample(BundleContext context) {
		this.context=context;
	}
	
	public void run() {
		try {
			StdSchedulerFactory factory = new StdSchedulerFactory();

			try {
				this.scheduler = factory.getScheduler();
			} catch (NoClassDefFoundError e) {
				this.logger.error(" Unable to load a class - most likely you do not have jta.jar on the classpath. If not present in the examples/lib folder, please " +
						"add it there for this sample to run.", e);
				return;
			}

			this.logger.info("------- Initialization Complete -----------");
			this.logger.info("------- (Not Scheduling any Jobs - relying on XML definitions --");
			this.logger.info("------- Starting Scheduler ----------------");

			// Start up the scheduler (nothing can actually run until the scheduler has been started)
			this.scheduler.start();

			this.logger.info("------- Started Scheduler -----------------");
			this.logger.info("------- Waiting five minutes... -----------");

			// wait five minutes to give our jobs a chance to run
			try {
				Thread.sleep(300L * 1000L);
			} catch (Exception e) {
				//
			}
		} catch (SchedulerException exception) {
			exception.printStackTrace();
		}
	}

	public void stopThread() {
		this.active = false;

		// shut down the scheduler
		this.logger.info("------- Shutting Down ---------------------");

		try {
			this.scheduler.shutdown(true);

			this.logger.info("------- Shutdown Complete -----------------");

			// display some stats about the schedule that just ran
			SchedulerMetaData metaData = this.scheduler.getMetaData();

			this.logger.info("Executed " + metaData.getNumberOfJobsExecuted() + " jobs.");
		} catch (SchedulerException exception) {
			exception.printStackTrace();
		}
	}
}