package osgi.component.configuration;

import java.util.Dictionary;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;

@Component(
		name="factory",
		property="service.pid=ConnectionServiceFactory",
	configurationPid = "ConnectionServiceFactory",
	configurationPolicy = ConfigurationPolicy.REQUIRE,
	service=org.osgi.service.cm.ManagedServiceFactory.class
)
public class ConnectionServiceFactory implements ManagedServiceFactory {
//	private Map<String, EchoServer> echoServers = new TreeMap<String, EchoServer>();
	
//	public static String PID = "service.pid=ConnectionServiceFactory";

	public String getName() {
		return "connection service factory";
	}

	@Override
	public void updated(String pid, Dictionary<String, ?> properties) throws ConfigurationException {
		System.out.println("creating connection service with pid " + pid);
		
//		Enumeration<String> enumeration = properties.keys();
//		
//        while(enumeration.hasMoreElements()) {
//            String k = enumeration.nextElement();
//            System.out.println(k + ": " + properties.get(k));
//        }
//        
//		System.out.println("wtf " + pid);
//		
//		if (properties != null) { 
//			String portString = properties.get("port").toString();
//
//			try {
//				int port = Integer.parseInt(portString);
//				System.out.println("Creating echo server on port " + port);
//
//				echoServers.put(pid, new EchoServer(port));
//			} catch (Exception e) {
//				throw new ConfigurationException("port", "Cannot create a server on port " + portString, e);
//			}
//		} else if (echoServers.containsKey(pid)) {
//			deleted(pid);
//		}
	}

	@Override
	public void deleted(String pid) {
		System.out.println("removing connection service with pid " + pid);
//		EchoServer removed = echoServers.remove(pid);
//		if (removed != null) {
//			removed.stop();
//		} 
	}
}