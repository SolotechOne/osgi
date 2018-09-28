package osgi.sap.service.provider.bapi.cm;

import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;

public class cm {
    public static void bapi_cm_criteria_get(JCoDestination destination, int id, String type) throws JCoException {
    	JCoFunction cm_criteria_get = destination.getRepository().getFunction("BAPI_CM_CRITERIA_GET");
    	
        if (cm_criteria_get == null)
        	throw new RuntimeException("BAPI_CM_CRITERIA_GET not found in SAP.");
        
        cm_criteria_get.getImportParameterList().setValue("I_EXTERNAL_USER_NAME", "AUDIT");
        cm_criteria_get.getImportParameterList().setValue("I_PROFILEID", id); 
        cm_criteria_get.getImportParameterList().setValue("I_PROFILETYPE", type); 
        
        cm_criteria_get.getExportParameterList().setActive("E_CRITERIA_XML", true);
        cm_criteria_get.getExportParameterList().setActive("RETURN", true);
        
        try {
        	cm_criteria_get.execute(destination);
        }
        catch (AbapException exception) {
            System.out.println(exception.toString());
            
            return;
        }
        
//        System.out.println("BAPI_CM_CRITERIA_GET finished:");
        
        JCoStructure bapiret = cm_criteria_get.getExportParameterList().getStructure("RETURN");
        
        if (! (bapiret.getString("TYPE").equals("") || bapiret.getString("TYPE").equals("S") || bapiret.getString("TYPE").equals("W")) ) {
            throw new RuntimeException(bapiret.getString("MESSAGE"));
        }
        
        System.out.println(cm_criteria_get.getExportParameterList().getString("E_CRITERIA_XML"));
//        System.out.println();
    }

    public static void bapi_cm_criteria_set(JCoDestination destination, int id, String type, String xml) throws JCoException {
		JCoFunction cm_criteria_get = destination.getRepository().getFunction("BAPI_CM_CRITERIA_SET");
		
	    if (cm_criteria_get == null)
	    	throw new RuntimeException("BAPI_CM_CRITERIA_SET not found in SAP.");
	    
	    cm_criteria_get.getImportParameterList().setValue("I_EXTERNAL_USER_NAME", "AUDIT");
	    cm_criteria_get.getImportParameterList().setValue("I_PROFILEID", id); 
	    cm_criteria_get.getImportParameterList().setValue("I_PROFILETYPE", type); 
	    cm_criteria_get.getImportParameterList().setValue("I_CRITERIA_XML", xml); 
	    
	    cm_criteria_get.getExportParameterList().setActive("RETURN", true);
	    
	    try {
	    	cm_criteria_get.execute(destination);
	    }
	    catch (AbapException exception) {
	        System.out.println(exception.toString());
	        
	        return;
	    }
	    
//	    System.out.println("BAPI_CM_CRITERIA_SET finished:");
	    
	    JCoStructure bapiret = cm_criteria_get.getExportParameterList().getStructure("RETURN");
	    
	    if (! (bapiret.getString("TYPE").equals("") || bapiret.getString("TYPE").equals("S") || bapiret.getString("TYPE").equals("W")) ) {
	        throw new RuntimeException(bapiret.getString("MESSAGE"));
	    }
	    
//	    System.out.println();
	}

	public static void bapi_cm_crittypes_get(JCoDestination destination, String mask) throws JCoException {
    	JCoFunction cm_crittypes_get = destination.getRepository().getFunction("BAPI_CM_CRITTYPES_GET");
    	
        if (cm_crittypes_get == null)
        	throw new RuntimeException("BAPI_CM_CRITTYPES_GET not found in SAP.");
        
        cm_crittypes_get.getImportParameterList().setValue("I_EXTERNAL_USER_NAME", "AUDIT");
        cm_crittypes_get.getImportParameterList().setValue("I_CRITTYPE", mask);	// default value '%' 
        
        cm_crittypes_get.getExportParameterList().setActive("E_T_CRITYTPES", true);
        cm_crittypes_get.getExportParameterList().setActive("RETURN", true);
        
        try {
        	cm_crittypes_get.execute(destination);
        }
        catch (AbapException exception) {
            System.out.println(exception.toString());
            
            return;
        }
        
//        System.out.println("BAPI_CM_CRITTYPES_GET finished:");
        
        JCoStructure bapiret = cm_crittypes_get.getExportParameterList().getStructure("RETURN");
        
        if (! (bapiret.getString("TYPE").equals("") || bapiret.getString("TYPE").equals("S") || bapiret.getString("TYPE").equals("W")) ) {
            throw new RuntimeException(bapiret.getString("MESSAGE"));
        }
        
        JCoTable types = cm_crittypes_get.getExportParameterList().getTable("E_T_CRITYTPES");
        
        for (int i = 0; i < types.getNumRows(); i++) {
        	types.setRow(i);
        	
        	System.out.println(types.getString("TYPE") + " " + types.getString("TYPEDESCRIPTION") + " " + types.getString("CALLBACK_PROG")
        			+ " " + types.getString("CALLBACK_FORM") + " " + types.getString("FIELD") + " " + types.getString("DESCRIPTION")
        			+ " " + types.getString("SEARCH_HELP"));
        }
        
        System.out.println();
        System.out.println("Criteria types selected: " + types.getNumRows());
//        System.out.println();
    }
    
    public static void bapi_cm_profiles_get(JCoDestination destination, String mask) throws JCoException {
    	JCoFunction cm_profiles_get = destination.getRepository().getFunction("BAPI_CM_PROFILES_GET");
    	
        if (cm_profiles_get == null)
        	throw new RuntimeException("BAPI_CM_PROFILES_GET not found in SAP.");
        
        cm_profiles_get.getImportParameterList().setValue("I_EXTERNAL_USER_NAME", "AUDIT");
        cm_profiles_get.getImportParameterList().setValue("I_PROFILETYPE", mask);	// default value '%' 
        
        cm_profiles_get.getExportParameterList().setActive("E_T_PROFILES", true);
        cm_profiles_get.getExportParameterList().setActive("RETURN", true);
        
        try {
        	cm_profiles_get.execute(destination);
        }
        catch (AbapException exception) {
            System.out.println(exception.toString());
            
            return;
        }
        
//        System.out.println("BAPI_CM_PROFILES_GET finished:");
        
        JCoStructure bapiret = cm_profiles_get.getExportParameterList().getStructure("RETURN");
        
        if (! (bapiret.getString("TYPE").equals("") || bapiret.getString("TYPE").equals("S") || bapiret.getString("TYPE").equals("W")) ) {
            throw new RuntimeException(bapiret.getString("MESSAGE"));
        }
        
        JCoTable profiles = cm_profiles_get.getExportParameterList().getTable("E_T_PROFILES");
        
        for (int i = 0; i < profiles.getNumRows(); i++) {
        	profiles.setRow(i);
        	
        	System.out.println(profiles.getString("ID") + " " + profiles.getString("TYPE") + " " + profiles.getString("DESCRIPTION")
        			+ " " + profiles.getString("STATE") + " " + profiles.getString("LASTCHUSER") + " " + profiles.getString("LASTCHTMSTMP")
        			+ " " + profiles.getString("TYPEDESCRIPTION") + " " + profiles.getString("CREATEUSER"));
        }
        
        System.out.println();
        System.out.println("Criteria types selected: " + profiles.getNumRows());
//        System.out.println();
    }

    public static void bapi_cm_profile_create(JCoDestination destination, String xml) throws JCoException {
    	JCoFunction cm_profiles_activate = destination.getRepository().getFunction("BAPI_CM_PROFILE_CREATE");
    	
        if (cm_profiles_activate == null)
        	throw new RuntimeException("BAPI_CM_PROFILE_CREATE not found in SAP.");
        
        cm_profiles_activate.getImportParameterList().setValue("I_EXTERNAL_USER_NAME", "AUDIT");
        cm_profiles_activate.getImportParameterList().setValue("I_PROFILE_XML", xml);
        
        cm_profiles_activate.getExportParameterList().setActive("E_PROFILEID", true); 
        cm_profiles_activate.getExportParameterList().setActive("E_PROFILETYPE", true); 
        cm_profiles_activate.getExportParameterList().setActive("RETURN", true);
        
        try {
        	cm_profiles_activate.execute(destination);
        }
        catch (AbapException exception) {
            System.out.println(exception.toString());
            
            return;
        }
        
//        System.out.println("BAPI_CM_PROFILE_CREATE finished:");
        
        JCoStructure bapiret = cm_profiles_activate.getExportParameterList().getStructure("RETURN");
        
        if (! (bapiret.getString("TYPE").equals("") || bapiret.getString("TYPE").equals("S") || bapiret.getString("TYPE").equals("W")) ) {
            throw new RuntimeException(bapiret.getString("MESSAGE"));
        }
        
//        System.out.println();
        System.out.println("Criteria Profile " + cm_profiles_activate.getExportParameterList().getString("E_PROFILETYPE")
        	+ " with ID " + cm_profiles_activate.getExportParameterList().getString("E_PROFILEID")
        	+ " created");
    }

    public static void bapi_cm_profile_activate(JCoDestination destination, int id, String type) throws JCoException {
    	JCoFunction cm_profiles_activate = destination.getRepository().getFunction("BAPI_CM_PROFILE_ACTIVATE");
    	
        if (cm_profiles_activate == null)
        	throw new RuntimeException("BAPI_CM_PROFILE_ACTIVATE not found in SAP.");
        
        cm_profiles_activate.getImportParameterList().setValue("I_EXTERNAL_USER_NAME", "AUDIT");
        cm_profiles_activate.getImportParameterList().setValue("I_PROFILEID", id); 
        cm_profiles_activate.getImportParameterList().setValue("I_PROFILETYPE", type); 
        
        cm_profiles_activate.getExportParameterList().setActive("RETURN", true);
        
        try {
        	cm_profiles_activate.execute(destination);
        }
        catch (AbapException exception) {
            System.out.println(exception.toString());
            
            return;
        }
        
//        System.out.println("BAPI_CM_PROFILE_ACTIVATE finished:");
        
        JCoStructure bapiret = cm_profiles_activate.getExportParameterList().getStructure("RETURN");
        
        if (! (bapiret.getString("TYPE").equals("") || bapiret.getString("TYPE").equals("S") || bapiret.getString("TYPE").equals("W")) ) {
            throw new RuntimeException(bapiret.getString("MESSAGE"));
        }
        
//        System.out.println();
    }

    public static void bapi_cm_profile_deactivate(JCoDestination destination, String type) throws JCoException {
    	JCoFunction cm_profiles_activate = destination.getRepository().getFunction("BAPI_CM_PROFILE_DEACTIVATE");
    	
        if (cm_profiles_activate == null)
        	throw new RuntimeException("BAPI_CM_PROFILE_DEACTIVATE not found in SAP.");
        
        cm_profiles_activate.getImportParameterList().setValue("I_EXTERNAL_USER_NAME", "AUDIT");
        cm_profiles_activate.getImportParameterList().setValue("I_PROFILETYPE", type); 
        
        cm_profiles_activate.getExportParameterList().setActive("RETURN", true);
        
        try {
        	cm_profiles_activate.execute(destination);
        }
        catch (AbapException exception) {
            System.out.println(exception.toString());
            
            return;
        }
        
//        System.out.println("BAPI_CM_PROFILE_DEACTIVATE finished:");
        
        JCoStructure bapiret = cm_profiles_activate.getExportParameterList().getStructure("RETURN");
        
        if (! (bapiret.getString("TYPE").equals("") || bapiret.getString("TYPE").equals("S") || bapiret.getString("TYPE").equals("W")) ) {
            throw new RuntimeException(bapiret.getString("MESSAGE"));
        }
        
//        System.out.println();
    }
    
    public static void bapi_cm_profile_delete(JCoDestination destination, int id, String type) throws JCoException {
    	JCoFunction cm_profiles_activate = destination.getRepository().getFunction("BAPI_CM_PROFILE_DELETE");
    	
        if (cm_profiles_activate == null)
        	throw new RuntimeException("BAPI_CM_PROFILE_DELETE not found in SAP.");
        
        cm_profiles_activate.getImportParameterList().setValue("I_EXTERNAL_USER_NAME", "AUDIT");
        cm_profiles_activate.getImportParameterList().setValue("I_PROFILEID", id); 
        cm_profiles_activate.getImportParameterList().setValue("I_PROFILETYPE", type); 
        
        cm_profiles_activate.getExportParameterList().setActive("RETURN", true);
        
        try {
        	cm_profiles_activate.execute(destination);
        }
        catch (AbapException exception) {
            System.out.println(exception.toString());
            
            return;
        }
        
//        System.out.println("BAPI_CM_PROFILE_DELETE finished:");
        
        JCoStructure bapiret = cm_profiles_activate.getExportParameterList().getStructure("RETURN");
        
        if (! (bapiret.getString("TYPE").equals("") || bapiret.getString("TYPE").equals("S") || bapiret.getString("TYPE").equals("W")) ) {
            throw new RuntimeException(bapiret.getString("MESSAGE"));
        }
        
//        System.out.println();
    }
}