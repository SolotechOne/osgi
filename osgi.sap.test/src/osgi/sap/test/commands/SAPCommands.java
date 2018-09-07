package osgi.sap.test.commands;

import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoRepository;
import com.sap.conn.jco.JCoTable;
import com.sap.conn.jco.ext.DestinationDataProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.Descriptor;

import org.osgi.service.component.annotations.Component;

@Component(
	property = {
		CommandProcessor.COMMAND_SCOPE + ":String=sap",
		CommandProcessor.COMMAND_FUNCTION + ":String=connect"
	},
	service = SAPCommands.class
)
public class SAPCommands {
	private final static String SAPApplicationServer = "cirk1.de.globusgrp.org";
	private final static String SAPSystemNumber = "00";
	@SuppressWarnings("unused")
	private final static String SAPSystem = "RK1-KON";
	@SuppressWarnings("unused")
	private final static String SAPMessageServer = "ascsrk1.de.globusgrp.org";
	@SuppressWarnings("unused")
	private final static String SAPGroup = "public";
	private final static String SAPClient = "200";
	private final static String SAPUser = "batch-uc4";
	private final static String SAPPassword = "uc4amh15";
	private final static String SAPLanguage = "de";
	
    @Descriptor("connect to sap")
    public void connect() throws IOException, JCoException {
    	System.out.println("connecting...");
    	
        Properties connectProperties = new Properties();
        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, SAPApplicationServer);
        connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,  SAPSystemNumber);
        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, SAPClient);
        connectProperties.setProperty(DestinationDataProvider.JCO_USER,   SAPUser);
        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, SAPPassword);
        connectProperties.setProperty(DestinationDataProvider.JCO_LANG,   SAPLanguage);
        
        createDestinationDataFile("RK1", connectProperties);
        
    	JCoDestination destination = JCoDestinationManager.getDestination("RK1");
    	
        System.out.println("Attributes:");
        System.out.println(destination.getAttributes());
    	
    	destination.ping();
    	
    	JCoFunction function = destination.getRepository().getFunction("STFC_CONNECTION");
    	
        if (function == null)
        	throw new RuntimeException("BAPI_COMPANYCODE_GETLIST not found in SAP.");
        
        function.getImportParameterList().setValue("REQUTEXT", "Hello SAP");
        
        try {
            function.execute(destination);
        }
        catch (AbapException e) {
            System.out.println(e.toString());
            
            return;
        }
        
        System.out.println("STFC_CONNECTION finished:");
        System.out.println(" Echo: " + function.getExportParameterList().getString("ECHOTEXT"));
        System.out.println(" Response: " + function.getExportParameterList().getString("RESPTEXT"));
        System.out.println();
        
        getVariantInfo(destination);
    }
    
    public void getVariantInfo(JCoDestination destination) throws JCoException {
    	JCoFunction xmi_logon = destination.getRepository().getFunction("BAPI_XMI_LOGON");
    	
        if (xmi_logon == null)
        	throw new RuntimeException("BAPI_XMI_LOGON not found in SAP.");
        
        xmi_logon.getImportParameterList().setValue("EXTCOMPANY", "testcompany");
        xmi_logon.getImportParameterList().setValue("EXTPRODUCT", "testproduct");
        xmi_logon.getImportParameterList().setValue("INTERFACE", "XBP");
        xmi_logon.getImportParameterList().setValue("VERSION", "3.0");
        
        xmi_logon.getExportParameterList().setActive("RETURN", false);
        xmi_logon.getExportParameterList().setActive("SESSIONID", false);
        
        try {
        	xmi_logon.execute(destination);
        }
        catch (AbapException e) {
            System.out.println(e.toString());
            
            return;
        }
        
        System.out.println("BAPI_XMI_LOGON erfolgreich");
        
        
        JCoTable codes = xmi_logon.getTableParameterList().getTable("COMPANYCODE_LIST");

        for (int i = 0; i < codes.getNumRows(); i++) 

        {

            codes.setRow(i);

            System.out.println(codes.getString("COMP_CODE") + '\t' + codes.getString("COMP_NAME"));

        }
    }
    
    static void createDestinationDataFile(String destinationName, Properties connectProperties) {
        File destCfg = new File(destinationName+".jcoDestination");
        
        System.out.println(destCfg.getAbsolutePath());
        
        try {
            FileOutputStream fos = new FileOutputStream(destCfg, false);
            connectProperties.store(fos, "for tests only !");
            fos.close();
        }
        
        catch (Exception e) {
            throw new RuntimeException("Unable to create the destination files", e);
        }
    }
}