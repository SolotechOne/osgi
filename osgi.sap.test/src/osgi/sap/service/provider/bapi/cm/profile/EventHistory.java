package osgi.sap.service.provider.bapi.cm.profile;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import osgi.sap.service.provider.criteria.profile.Profile;

public class EventHistory {
	public static String empty() throws TransformerFactoryConfigurationError, TransformerException, ParserConfigurationException {
	    // Create document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        
        Document doc = builder.newDocument();
        doc.setXmlStandalone(true);
        
        //Create doc type
//        DOMImplementation domImpl = doc.getImplementation();
//        DocumentType doctype = domImpl.createDocumentType("profile", "-//CompanyName//DTD CompanyName PaymentService v2//EN", "criteria_profile.dtd");
//        doc.appendChild(doctype);
        
        // Add profile element
        Element profile = doc.createElement("profile");
        profile.setAttribute("type", "EVTHIS");
//        profile.setAttribute("id", "0");
        profile.setAttribute("description", "test");
//        profile.setAttribute("state", "");
//        profile.setAttribute("lastchuser", "BATCH-UC4");
//        profile.setAttribute("lastchtmstmp", "20180925125220");
//        profile.setAttribute("createuser", "BERBERICH-CA");
        
        // Add root element
        Element root = doc.createElement("root");

        // Add node element
//        Element node = doc.createElement("node");
//        node.setAttribute("type", "O");

        // Add item element
//        Element item = doc.createElement("item");
//        item.setAttribute("description", "bestaetigt");

        // Add eventid element
//        Element eventid = doc.createElement("field");
//        eventid.setTextContent("EVENTID");
        
        // Add criterion element
//        Element criterion = doc.createElement("criterion");
//        criterion.setAttribute("sign", "I");
//        criterion.setAttribute("opt", "EQ");
//        criterion.setAttribute("low", "*");
//        criterion.setAttribute("high", "");
        
//        eventid.appendChild(criterion);

//        item.appendChild(eventid);

        // Add eventparm element
//        Element eventparm = doc.createElement("field");
//        eventparm.setTextContent("EVENTPARM");

//        item.appendChild(eventparm);

//        node.appendChild(item);

//        root.appendChild(node);

        profile.appendChild(root);
        
        doc.appendChild(profile);
        
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
//        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "true");
//        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "profile");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "criteria_profile.dtd");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");

        StringWriter sw = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(sw));
//        System.out.print(sw.toString());  
        
        return sw.toString();
	}
	
	public static String create() throws ParserConfigurationException, UnsupportedEncodingException, TransformerException {
//	    String empty_profile ="<?xml version=\"1.0\"?> <!DOCTYPE profile SYSTEM \"criteria_profile.dtd\">"
//	    		+ "<profile type=\"EVTHIS\" id=\"1\" description=\"test\">"
//	    		+ "<root/>"
//	    		+ "</profile>";
	    
//	    String evthis_profile = "?<?xml version=\"1.0\" encoding=\"utf-16\"?><!DOCTYPE profile SYSTEM \"criteria_profile.dtd\"> <profile type=\"EVTHIS\" id=\"1 \" description=\"\" state=\"\" lastchuser=\"BATCH-UC4\" lastchtmstmp=\"20180925125220 \" createuser=\"BERBERICH-CA\"><root><node type=\"O\"><item description=\"test\"><field>EVENTID<criterion sign=\"I\" opt=\"EQ\" low=\"*\" high=\"\"/></field><field>EVENTPARM</field></item></node></root></profile>";
		
	    // Create document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        
        DocumentBuilder builder = factory.newDocumentBuilder();
        
        Document doc = builder.newDocument();
        doc.setXmlStandalone(true);
        
        //Create doc type
//        DOMImplementation domImpl = doc.getImplementation();
//        DocumentType doctype = domImpl.createDocumentType("profile", "-//CompanyName//DTD CompanyName PaymentService v2//EN", "criteria_profile.dtd");
//        doc.appendChild(doctype);
        
        // Add profile element
        Element profile = doc.createElement("profile");
        profile.setAttribute("type", "EVTHIS");
        profile.setAttribute("id", "0");
        profile.setAttribute("description", "test");
        profile.setAttribute("state", "");
        profile.setAttribute("lastchuser", "BATCH-UC4");
        profile.setAttribute("lastchtmstmp", "20180925125220");
        profile.setAttribute("createuser", "BERBERICH-CA");
        
        // Add root element
        Element root = doc.createElement("root");
        
        // Add node element
        Element node = doc.createElement("node");
        node.setAttribute("type", "O");
        
        // Add item element
        Element item = doc.createElement("item");
        item.setAttribute("description", "bestaetigt");
        
        // Add eventid element
        Element eventid = doc.createElement("field");
        eventid.setTextContent("EVENTID");
        
        // Add criterion element
        Element criterion = doc.createElement("criterion");
        criterion.setAttribute("sign", "I");
        criterion.setAttribute("opt", "EQ");
        criterion.setAttribute("low", "*");
        criterion.setAttribute("high", "");
        
        eventid.appendChild(criterion);
        
        item.appendChild(eventid);
        
        // Add eventparm element
        Element eventparm = doc.createElement("field");
        eventparm.setTextContent("EVENTPARM");
        
        item.appendChild(eventparm);
        
        node.appendChild(item);
        
        root.appendChild(node);
        
        profile.appendChild(root);
        
        doc.appendChild(profile);
        
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
//        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "true");
//        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "profile");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "criteria_profile.dtd");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
        
        // send DOM to file
//        transformer.transform(new DOMSource(doc), new StreamResult(new OutputStreamWriter(System.out, "UTF-8")));
        
        // send DOM to String
        StringWriter sw = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(sw));
//        System.out.print(sw.toString());  
        
        return sw.toString();
    }
	
	public static void validate() {
	    String evthis_profile = "<?xml version=\"1.0\" encoding=\"utf-16\"?><!DOCTYPE profile SYSTEM \"criteria_profile.dtd\"> <profile type=\"EVTHIS\" id=\"1 \" description=\"\" state=\"\" lastchuser=\"BATCH-UC4\" lastchtmstmp=\"20180925125220 \" createuser=\"BERBERICH-CA\"><root><node type=\"O\"><item description=\"test\"><field>EVENTID<criterion sign=\"I\" opt=\"EQ\" low=\"*\" high=\"\"/></field><field>EVENTPARM</field></item></node></root></profile>";
	    
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        
        DocumentBuilder builder;
        
        try {
			builder = factory.newDocumentBuilder();
	        
			builder.setErrorHandler(new ErrorHandler() {
				@Override
				public void error(SAXParseException exception) throws SAXException {
					exception.printStackTrace();
				}
				@Override
				public void fatalError(SAXParseException exception) throws SAXException {
					exception.printStackTrace();
				}
				
				@Override
				public void warning(SAXParseException exception) throws SAXException {
					exception.printStackTrace();
				}
			});
			
	        try {
				Document document = builder.parse(new InputSource(new StringReader(evthis_profile)));
				
				document.getDocumentElement().normalize();
				
				System.out.println("root element: " + document.getDocumentElement().getNodeName());
				
//				Element element = document.getDocumentElement();
				
				NodeList nodelist = document.getElementsByTagName("*");
//				NodeList nodelist = document.getDocumentElement().getChildNodes();
				
				int length = nodelist.getLength();
				
		        for (int i = 0; i < length; i++) {
		        	Element el = (Element) nodelist.item(i);
		        	
		            if (el.getNodeType() == Node.ELEMENT_NODE) {
		                
		                System.out.println("name: " + el.getNodeName() + " value: " + el.getNodeValue() + " text: " + el.getTextContent());
		                
//		                if (el.getNodeName().contains("staff")) {
//		                    String name = el.getElementsByTagName("name").item(0).getTextContent();
//		                    String phone = el.getElementsByTagName("phone").item(0).getTextContent();
//		                    String email = el.getElementsByTagName("email").item(0).getTextContent();
//		                    String area = el.getElementsByTagName("area").item(0).getTextContent();
//		                    String city = el.getElementsByTagName("city").item(0).getTextContent();
//		                }
		            }
		        }
				
		        Transformer transformer;
		        
				try {
					transformer = TransformerFactory.newInstance().newTransformer();

			        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//			        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
//			        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//			        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "true");
//			        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "profile");
			        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "criteria_profile.dtd");
			        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");

			        StringWriter sw = new StringWriter();
			        
			        try {
						transformer.transform(new DOMSource(document), new StreamResult(sw));
					} catch (TransformerException exception) {
						exception.printStackTrace();
					}
			        
			        System.out.print(sw.toString());
				} catch (TransformerConfigurationException exception) {
					exception.printStackTrace();
				} catch (TransformerFactoryConfigurationError exception) {
					exception.printStackTrace();
				}
			} catch (SAXException exception) {
				exception.printStackTrace();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		} catch (ParserConfigurationException exception) {
			exception.printStackTrace();
		}
	}
	
	public static String marshal(Profile profile) {
		try {
			JAXBContext context = JAXBContext.newInstance(Profile.class);
			
			try {
				Marshaller marshaller = context.createMarshaller();
				
				StringWriter writer = new StringWriter();
				
				marshaller.marshal(profile, writer);
				
				return writer.toString();
			} catch (JAXBException exception) {
				exception.printStackTrace();
			}
		} catch (JAXBException exception) {
			exception.printStackTrace();
		}
		
		return null;
	}
	
	public static Profile unmarshal(String xml) {
	    try {
			JAXBContext context = JAXBContext.newInstance(Profile.class);
			
			XMLInputFactory xif = XMLInputFactory.newFactory();
//	        xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);

	        try {
//				XMLStreamReader xsr = xif.createXMLStreamReader(new StreamSource("input.xml"));
				XMLStreamReader xsr = xif.createXMLStreamReader(new StringReader(xml));

				Unmarshaller unmarshaller = context.createUnmarshaller();

				Profile profile = (Profile) unmarshaller.unmarshal(xsr);
				return profile;
	        } catch (XMLStreamException exception) {
	        	exception.printStackTrace();
	        }
		} catch (JAXBException exception) {
			exception.printStackTrace();
		}
	    
		return null;
	}
}