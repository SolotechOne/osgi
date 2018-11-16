import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.sap.conn.jco.ext.DestinationDataProvider;

public class Activator implements BundleActivator {
	private final static String SAPApplicationServer = "cirk1.de.globusgrp.org";
	private final static String SAPSystemNumber = "00";
	
	@SuppressWarnings("unused")
	private final static String SAPSystem = "RK1-KON";

	@SuppressWarnings("unused")
	private final static String SAPMessageServer = "ascsrk1.de.globusgrp.org";

	
//	private final static String SAPApplicationServer = "cirp1.de.globusgrp.org";
//	private final static String SAPSystemNumber = "06";
//
//	@SuppressWarnings("unused")
//	private final static String SAPSystem = "RP1-PRD";
//
//	@SuppressWarnings("unused")
//	private final static String SAPMessageServer = "ascsrp1.de.globusgrp.org";

	
	@SuppressWarnings("unused")
	private final static String SAPGroup = "public";
	private final static String SAPClient = "200";
	private final static String SAPUser = "batch-uc4";
	private final static String SAPPassword = "uc4amh15";
	private final static String SAPLanguage = "de";

	@Override
	public void start(BundleContext context) throws Exception {
		Properties connectProperties = new Properties();

		connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, SAPApplicationServer);
		connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,  SAPSystemNumber);
		connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, SAPClient);
		connectProperties.setProperty(DestinationDataProvider.JCO_USER,   SAPUser);
		connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, SAPPassword);
		connectProperties.setProperty(DestinationDataProvider.JCO_LANG,   SAPLanguage);

//		Util.createDestinationDataFile("RK1", connectProperties);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
	}
}