// @javdoc https://help.hana.ondemand.com/javadoc/index.html?com/sap/conn/jco/JCo.html

package osgi.sap.service.provider.commands;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBElement;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;

import osgi.sap.service.provider.bapi.cm.cm;
import osgi.sap.service.provider.bapi.cm.profile.EventHistory;
import osgi.sap.service.provider.bapi.xmi.xmi;
import osgi.sap.service.provider.criteria.profile.ObjectFactory;
import osgi.sap.service.provider.criteria.profile.Criterion;
import osgi.sap.service.provider.criteria.profile.Field;
import osgi.sap.service.provider.criteria.profile.Item;
import osgi.sap.service.provider.criteria.profile.Node;
import osgi.sap.service.provider.criteria.profile.Profile;
import osgi.sap.service.provider.criteria.profile.Root;

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
	private static JCoDestination destination;

	@Activate
	void activate() throws JCoException {
		//        System.out.println("criteria commands activated");

		//        Properties connectProperties = new Properties();
		//        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, SAPApplicationServer);
		//        connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,  SAPSystemNumber);
		//        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, SAPClient);
		//        connectProperties.setProperty(DestinationDataProvider.JCO_USER,   SAPUser);
		//        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, SAPPassword);
		//        connectProperties.setProperty(DestinationDataProvider.JCO_LANG,   SAPLanguage);
		//        
		//        Util.createDestinationDataFile("RK1", connectProperties);
		//        
		//    	destination = JCoDestinationManager.getDestination("RK1");

		//    	List<String> destinationIDs = JCo.getDestinationIDs();
		//    	System.out.println(destinationIDs);

		//    	System.out.println("JCo Version: " + JCo.getVersion());

		//    	JCo.setTrace(5, "stdout");		// or use '-Djco.trace_level=5 -Djco.trace_path=stdout'

		//        JCoContext.begin(destination);

		destination = JCoDestinationManager.getDestination("RK1");

		JCoContext.begin(destination);
	}

	@Deactivate
	void deactivate() throws JCoException {
		JCoContext.end(destination);
	}

	@Descriptor("get a list of available criteria types")
	public void types(@Parameter(names={"-m","--mask"}, absentValue="%") @Descriptor("Mask") String mask) throws JCoException {
		try {
			xmi.bapi_xmi_logon(destination);

			cm.bapi_cm_crittypes_get(destination, mask);
		}
		finally {
			xmi.bapi_xmi_logoff(destination);
		}
	}

	@Descriptor("get criteria table and tree")
	public String get(@Descriptor("Type") String type, @Descriptor("ID") int id) throws JCoException {
		try {
			xmi.bapi_xmi_logon(destination);

			return cm.bapi_cm_criteria_get(destination, id, type);
		}
		finally {
			xmi.bapi_xmi_logoff(destination);
		}
	}

	@Descriptor("update criteria table and tree")
	public void set(@Descriptor("Type") String type, @Descriptor("ID") int id, @Descriptor("XML") String xml) throws JCoException {
		try {
			xmi.bapi_xmi_logon(destination);

			cm.bapi_cm_criteria_set(destination, id, type, EventHistory.create());
		} catch (ParserConfigurationException | TransformerException | UnsupportedEncodingException exception) {
			exception.printStackTrace();
		}
		finally {
			xmi.bapi_xmi_logoff(destination);
		}
	}

	@Descriptor("get a list of criteria profiles")
	public void profiles(@Parameter(names={"-m","--mask"}, absentValue="%") @Descriptor("Mask") String mask) throws JCoException {
		try {
			xmi.bapi_xmi_logon(destination);

			cm.bapi_cm_profiles_get(destination, mask);
		}
		finally {
			xmi.bapi_xmi_logoff(destination);
		}
	}

	@Descriptor("create criteria profile")
	public void create(@Descriptor("XML") String xml) throws JCoException {
		try {
			xmi.bapi_xmi_logon(destination);

			cm.bapi_cm_profile_create(destination, EventHistory.empty());
		} catch (TransformerFactoryConfigurationError | TransformerException | ParserConfigurationException exception) {
			exception.printStackTrace();
		}
		finally {
			xmi.bapi_xmi_logoff(destination);
		}
	}

	@Descriptor("activate criteria profile")
	public void activate(@Descriptor("Type") String type, @Descriptor("ID") int id) throws JCoException {
		try {
			xmi.bapi_xmi_logon(destination);

			cm.bapi_cm_profile_activate(destination, id, type);
		}
		finally {
			xmi.bapi_xmi_logoff(destination);
		}
	}

	@Descriptor("deactivate criteria profile")
	public void deactivate(@Descriptor("Type") String type) throws JCoException {
		try {
			xmi.bapi_xmi_logon(destination);

			cm.bapi_cm_profile_deactivate(destination, type);
		}
		finally {
			xmi.bapi_xmi_logoff(destination);
		}
	}

	@Descriptor("delete criteria profile")
	public void delete(@Descriptor("Type") String type, @Descriptor("ID") int id) throws JCoException {
		try {
			xmi.bapi_xmi_logon(destination);

			cm.bapi_cm_profile_delete(destination, id, type);
		}
		finally {
			xmi.bapi_xmi_logoff(destination);
		}
	}

	@Descriptor("validate criteria profile")
	public void validate(String file) {
		//		String interc = get("INTERC", 1);

		//		System.out.println(String.format("%040x", new BigInteger(1, interc.substring(0, 10).getBytes("utf-16"))));
		//		System.out.println(new String(interc.getBytes("utf-16")));

		//		EventHistory.val(interc.substring(1));

		//		String input = marshal();
		//		EventHistory.val(input);

		InputStream stream = getClass().getResourceAsStream(file);

		EventHistory.validate(stream);
	}

	@Descriptor("marshal criteria profile")
	public String marshal() {
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

		root.getNode().add(node);

		Item item = new Item();
		item.setDescription("Log all Events");

		node.getItem().add(item);

		Field eventid = new Field();
		eventid.getContent().add(new String("EVENTID"));

		item.getField().add(eventid);

		Criterion criterion = new Criterion();
		criterion.setOpt("EQ");
		criterion.setSign("I");
		criterion.setLow("*");
		criterion.setHigh("");

		ObjectFactory of = new ObjectFactory();

		Field eventparm = new Field();
		eventparm.getContent().add(new String("EVENTPARM"));
		eventparm.getContent().add(of.createFieldCriterion(criterion));
		//		eventparm.getContent().add(of.createFieldCriterion(criterion));

		item.getField().add(eventparm);

		String output = EventHistory.marshal(profile);

		//		System.out.print(output);

		return output;
	}

	@Descriptor("unmarshal criteria profile")
	public void unmarshal(String file) {
		//		String xml = get("INTERC", 1);
		//		Profile profile = EventHistory.unmarshal(xml.substring(1));

		InputStream stream = getClass().getResourceAsStream(file);

		//			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		//			
		//			String input;
		//
		//			while ((input = reader.readLine()) != null) {
		//				System.out.println("<<<" + input + ">>>");
		//			}
		//
		//			reader.close();

		Profile profile = EventHistory.unmarshal(stream);

		recurse(profile);
	}

	private void recurse(Object obj) {
		switch (obj.getClass().getSimpleName()) {
		case "Profile":
			Profile profile = (Profile) obj;

			System.out.println("profile type: " + profile.getType()
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
			Root root = (Root) obj;

			System.out.println("root");

			for (Object item: root.getItem()) {
				recurse(item);
			}

			for (Object item: root.getNode()) {
				recurse(item);
			}

			break;
		case "Field":
			Field field = (Field) obj;

			//			System.out.println("      field:");

			for (Object nodes: field.getContent()) {
				recurse(nodes);
			}

			break;
		case "Criterion":
			Criterion criterion = (Criterion) obj;

			System.out.println("        criterion option: " + criterion.getOpt()
			+ " sign: " + criterion.getSign()
			+ " low: " + criterion.getLow()
			+ " high: " + criterion.getHigh());

			break;
		case "Node":
			Node node = (Node) obj;

			System.out.println("  node type: " + node.getType());

			for (Object nodes: node.getItem()) {
				recurse(nodes);
			}

			for (Object nodes: node.getNode()) {
				recurse(nodes);
			}

			break;
		case "Item":
			Item item = (Item) obj;

			System.out.println("    item: " + item.getDescription());

			for (Object fields: item.getField()) {
				recurse(fields);
			}

			break;
		case "String":
			System.out.println("      field: " + obj);

			break;
		case "JAXBElement":
			JAXBElement elem = (JAXBElement) obj;

			//			System.out.println(elem.getDeclaredType());

			recurse(elem.getValue());

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