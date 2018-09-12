package osgi.sap.service.provider.bapi;

import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoStructure;

public class xmi {
    public static void bapi_xmi_logon(JCoDestination destination) throws JCoException {
    	JCoFunction xmi_logon = destination.getRepository().getFunction("BAPI_XMI_LOGON");
    	
        if (xmi_logon == null)
        	throw new RuntimeException("BAPI_XMI_LOGON not found in SAP.");
        
        xmi_logon.getImportParameterList().setValue("EXTCOMPANY", "testcompany");
        xmi_logon.getImportParameterList().setValue("EXTPRODUCT", "testproduct");
        xmi_logon.getImportParameterList().setValue("INTERFACE", "XBP");
        xmi_logon.getImportParameterList().setValue("VERSION", "3.0");
        
        xmi_logon.getExportParameterList().setActive("RETURN", true);
        xmi_logon.getExportParameterList().setActive("SESSIONID", true);
        
        try {
        	xmi_logon.execute(destination);
        }
        catch (AbapException exception) {
            System.out.println(exception.toString());
            
            return;
        }
        
        System.out.println("BAPI_XMI_LOGON finished:");
        
        JCoStructure returnStructure = xmi_logon.getExportParameterList().getStructure("RETURN");
        
        if (! (returnStructure.getString("TYPE").equals("") || returnStructure.getString("TYPE").equals("S") || returnStructure.getString("TYPE").equals("W")) ) {
            throw new RuntimeException(returnStructure.getString("MESSAGE"));
        }
        
        if ( xmi_logon.getExportParameterList().isActive("SESSIONID")) {
            System.out.println(" SessionID: " + xmi_logon.getExportParameterList().getString("SESSIONID"));
        }
        
        System.out.println();
    }
    
    public static void bapi_xmi_logoff(JCoDestination destination) throws JCoException {
    	JCoFunction xmi_logoff = destination.getRepository().getFunction("BAPI_XMI_LOGOFF");
    	
        if (xmi_logoff == null)
        	throw new RuntimeException("BAPI_XMI_LOGOFF not found in SAP.");
        
        xmi_logoff.getImportParameterList().setValue("INTERFACE", "XBP");
        
        xmi_logoff.getExportParameterList().setActive("RETURN", true);
        
        try {
        	xmi_logoff.execute(destination);
        }
        catch (AbapException exception) {
            System.out.println(exception.toString());
            
            return;
        }
        
        System.out.println("BAPI_XMI_LOGOFF finished:");
        
        JCoStructure returnStructure = xmi_logoff.getExportParameterList().getStructure("RETURN");
        
        if (! (returnStructure.getString("TYPE").equals("") || returnStructure.getString("TYPE").equals("S") || returnStructure.getString("TYPE").equals("W")) ) {
            throw new RuntimeException(returnStructure.getString("MESSAGE"));
        }
        
        System.out.println();
    }
}