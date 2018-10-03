// @javdoc https://help.hana.ondemand.com/javadoc/index.html?com/sap/conn/jco/JCo.html

package osgi.sap.service.provider.commands;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import com.sap.conn.jco.JCo;
import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.ext.DestinationDataProvider;

import osgi.sap.service.provider.bapi.cm.cm;
import osgi.sap.service.provider.bapi.cm.profile.EventHistory;
import osgi.sap.service.provider.bapi.xmi.xmi;
import osgi.sap.service.provider.criteria.profile.Criterion;
import osgi.sap.service.provider.criteria.profile.Field;
import osgi.sap.service.provider.criteria.profile.Item;
import osgi.sap.service.provider.criteria.profile.Node;
import osgi.sap.service.provider.criteria.profile.Profile;
import osgi.sap.service.provider.criteria.profile.Root;
import osgi.sap.service.provider.util.Util;

@Component(
	property = {
		CommandProcessor.COMMAND_SCOPE + ":String=criteria",
		CommandProcessor.COMMAND_FUNCTION + ":String=types",
		CommandProcessor.COMMAND_FUNCTION + ":String=get",
		CommandProcessor.COMMAND_FUNCTION + ":String=set",
		CommandProcessor.COMMAND_FUNCTION + ":String=profiles",
		CommandProcessor.COMMAND_FUNCTION + ":String=create",
		CommandProcessor.COMMAND_FUNCTION + ":String=activate",
		CommandProcessor.COMMAND_FUNCTION + ":String=deactivate",
		CommandProcessor.COMMAND_FUNCTION + ":String=delete",
		CommandProcessor.COMMAND_FUNCTION + ":String=validate",
		CommandProcessor.COMMAND_FUNCTION + ":String=marshal",
		CommandProcessor.COMMAND_FUNCTION + ":String=unmarshal"
	},
	service = CriteriaCommands.class
)
public class CriteriaCommands {
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
	
	private static JCoDestination destination;
	
	@Activate
    void activate() throws JCoException {
//        System.out.println("criteria commands activated");
		
        Properties connectProperties = new Properties();
        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, SAPApplicationServer);
        connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,  SAPSystemNumber);
        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, SAPClient);
        connectProperties.setProperty(DestinationDataProvider.JCO_USER,   SAPUser);
        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, SAPPassword);
        connectProperties.setProperty(DestinationDataProvider.JCO_LANG,   SAPLanguage);
        
        Util.createDestinationDataFile("RK1", connectProperties);
        
    	destination = JCoDestinationManager.getDestination("RK1");
    	
//    	List<String> destinationIDs = JCo.getDestinationIDs();
//    	System.out.println(destinationIDs);

//    	System.out.println("JCo Version: " + JCo.getVersion());
    	
//    	JCo.setTrace(5, "stdout");		// or use '-Djco.trace_level=5 -Djco.trace_path=stdout'
    	
//        JCoContext.begin(destination);
	}
	
	@Deactivate
	void deactivate() throws JCoException {
//        System.out.println("criteria commands deactivated");

//        JCoContext.end(destination);
	}
	
	@Descriptor("get a list of available criteria types")
	public void types(@Parameter(names={"-m","--mask"}, absentValue="%") @Descriptor("Mask") String mask) throws IOException, JCoException {
		try {
			JCoContext.begin(destination);

			try {
				xmi.bapi_xmi_logon(destination);

				cm.bapi_cm_crittypes_get(destination, mask);
			}
			finally {
				xmi.bapi_xmi_logoff(destination);
			}
		}
		finally {
			JCoContext.end(destination);
		}
	}

	@Descriptor("get criteria table and tree")
	public void get(@Descriptor("Type") String type, @Descriptor("ID") int id) throws IOException, JCoException {
		try {
			JCoContext.begin(destination);

			try {
				xmi.bapi_xmi_logon(destination);

				cm.bapi_cm_criteria_get(destination, id, type);
			}
			finally {
				xmi.bapi_xmi_logoff(destination);
			}
		}
		finally {
			JCoContext.end(destination);
		}
	}

	@Descriptor("update criteria table and tree")
	public void set(@Descriptor("Type") String type, @Descriptor("ID") int id, @Descriptor("XML") String xml) throws IOException, JCoException {
		try {
			JCoContext.begin(destination);

			try {
				xmi.bapi_xmi_logon(destination);

				cm.bapi_cm_criteria_set(destination, id, type, EventHistory.create());
			} catch (ParserConfigurationException | TransformerException e) {
				e.printStackTrace();
			}
			finally {
				xmi.bapi_xmi_logoff(destination);
			}
		}
		finally {
			JCoContext.end(destination);
		}
	}

	@Descriptor("get a list of criteria profiles")
	public void profiles(@Parameter(names={"-m","--mask"}, absentValue="%") @Descriptor("Mask") String mask) throws IOException, JCoException {
		try {
			JCoContext.begin(destination);

			try {
				xmi.bapi_xmi_logon(destination);

				cm.bapi_cm_profiles_get(destination, mask);
			}
			finally {
				xmi.bapi_xmi_logoff(destination);
			}
		}
		finally {
			JCoContext.end(destination);
		}
	}

	@Descriptor("create criteria profile")
	public void create(@Descriptor("XML") String xml) throws IOException, JCoException {
		try {
			JCoContext.begin(destination);

			try {
				xmi.bapi_xmi_logon(destination);

				cm.bapi_cm_profile_create(destination, EventHistory.empty());
			} catch (TransformerFactoryConfigurationError | TransformerException | ParserConfigurationException e) {
				e.printStackTrace();
			}
			finally {
				xmi.bapi_xmi_logoff(destination);
			}
		}
		finally {
			JCoContext.end(destination);
		}
	}

	@Descriptor("activate criteria profile")
	public void activate(@Descriptor("Type") String type, @Descriptor("ID") int id) throws IOException, JCoException {
		try {
			JCoContext.begin(destination);

			try {
				xmi.bapi_xmi_logon(destination);

				cm.bapi_cm_profile_activate(destination, id, type);
			}
			finally {
				xmi.bapi_xmi_logoff(destination);
			}
		}
		finally {
			JCoContext.end(destination);
		}
	}

	@Descriptor("deactivate criteria profile")
	public void deactivate(@Descriptor("Type") String type) throws IOException, JCoException {
		try {
			JCoContext.begin(destination);

			try {
				xmi.bapi_xmi_logon(destination);

				cm.bapi_cm_profile_deactivate(destination, type);
			}
			finally {
				xmi.bapi_xmi_logoff(destination);
			}
		}
		finally {
			JCoContext.end(destination);
		}
	}

	@Descriptor("delete criteria profile")
	public void delete(@Descriptor("Type") String type, @Descriptor("ID") int id) throws IOException, JCoException {
		try {
			JCoContext.begin(destination);

			try {
				xmi.bapi_xmi_logon(destination);

				cm.bapi_cm_profile_delete(destination, id, type);
			}
			finally {
				xmi.bapi_xmi_logoff(destination);
			}
		}
		finally {
			JCoContext.end(destination);
		}
	}

	@Descriptor("validate criteria profile")
	public void validate() throws IOException, JCoException {
		EventHistory.val();
	}

	@Descriptor("marshal criteria profile")
	public void marshal() throws IOException, JCoException {
		Profile profile = new Profile();
		profile.setType("EVTHIS");
		profile.setId("0");
		profile.setDescription("Eventhistory Profile");
		profile.setCreateuser("BERBERICH-CA");
		profile.setLastchtmstmp("20181002135800");
		profile.setLastchuser("BERBERICH-CA");
		profile.setState("X");
		
		Root root = new Root();
		
		profile.setRoot(root);

		Node node = new Node();
		node.setType("A");
		
		root.getItemOrNode().add(node);
		
		Item item = new Item();
		item.setDescription("Log all Events");
		
		node.getItemOrNode().add(item);
		
		Field eventid = new Field();
		eventid.setvalue("EVENTID");
		
		item.getField().add(eventid);
		
		Criterion criterion = new Criterion();
		criterion.setOpt("EQ");
		criterion.setSign("I");
		criterion.setLow("*");
		criterion.setHigh("");

		Field eventparm = new Field();
		eventparm.setvalue("EVENTPARM");
		
		item.getField().add(eventparm);

		String output = EventHistory.marshal(profile);
		
		System.out.println(output);
	}

	@Descriptor("unmarshal criteria profile")
	public void unmarshal() throws IOException, JCoException {
	    String xml = "<?xml version=\"1.0\" encoding=\"utf-16\"?><!DOCTYPE profile SYSTEM \"criteria_profile.dtd\"> <profile type=\"EVTHIS\" id=\"1 \" description=\"\" state=\"\" lastchuser=\"BATCH-UC4\" lastchtmstmp=\"20180925125220 \" createuser=\"BERBERICH-CA\"><root><node type=\"O\"><item description=\"test\"><field>EVENTID<criterion sign=\"I\" opt=\"EQ\" low=\"*\" high=\"\"/></field><field>EVENTPARM</field></item></node></root></profile>";

		Profile profile = EventHistory.unmarshal(xml);
		
		recurse(profile);
	}
	
	private void recurse(Object obj) {
		switch (obj.getClass().getSimpleName()) {
		case "Profile":
			osgi.sap.service.provider.criteria.profile.Profile profile = (osgi.sap.service.provider.criteria.profile.Profile) obj;

			System.out.println("type: " + profile.getType()
				+ " id: " + profile.getId()
				+ " create user: " + profile.getCreateuser()
				+ " last changed: " + profile.getLastchtmstmp()
				+ " last user changed: " + profile.getLastchuser()
				+ " state: " + profile.getState()
				+ " description: " + profile.getDescription()
			);
			
			recurse(profile.getRoot());
			
			break;
		case "Root":
			osgi.sap.service.provider.criteria.profile.Root root = (osgi.sap.service.provider.criteria.profile.Root) obj;

			System.out.println("root");
			
			for (Object item: root.getItemOrNode()) {
				recurse(item);
			}
			
			break;
		case "Field":
			osgi.sap.service.provider.criteria.profile.Field field = (osgi.sap.service.provider.criteria.profile.Field) obj;

			System.out.println("field: " + field.getvalue());
			
			break;
		case "Criterion":
			osgi.sap.service.provider.criteria.profile.Criterion criterion = (osgi.sap.service.provider.criteria.profile.Criterion) obj;

			System.out.println("option: " + criterion.getOpt());
			System.out.println("sign: " + criterion.getSign());
			System.out.println("low: " + criterion.getLow());
			System.out.println("high: " + criterion.getHigh());
			
			break;
		case "Node":
			osgi.sap.service.provider.criteria.profile.Node node = (osgi.sap.service.provider.criteria.profile.Node) obj;

			System.out.println("node type: " + node.getType());

			for (Object nodes: node.getItemOrNode()) {
				recurse(nodes);
			}

			break;
		case "Item":
			osgi.sap.service.provider.criteria.profile.Item item = (osgi.sap.service.provider.criteria.profile.Item) obj;

			System.out.println("item: " + item.getDescription());
			
			for (Object fields: item.getField()) {
				recurse(fields);
			}
			
			break;
		default:
			throw new IllegalArgumentException(obj.getClass().getSimpleName());
		}
		

//			Class clazz = obj.getClass();
//			System.out.println(clazz.getName() + " " + clazz.getSimpleName());

//			if (obj instanceof osgi.sap.service.provider.criteria.profile.Node) {
//				System.out.println("its a node");
//			}
	}
}