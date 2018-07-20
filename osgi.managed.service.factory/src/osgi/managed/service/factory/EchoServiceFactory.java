package osgi.managed.service.factory;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;
import org.osgi.service.component.annotations.Component;
import osgi.managed.service.EchoServer;

@Component(
	property={"service.pid=osgi.managed.service.factory.EchoServiceFactory"},
	service=org.osgi.service.cm.ManagedServiceFactory.class
)
public class EchoServiceFactory implements ManagedServiceFactory {
	private Map<String, EchoServer> echoServers = new TreeMap<String, EchoServer>();
	
	public static String PID = "service.pid=osgi.managed.service.factory.EchoServiceFactory";

	public String getName() {
		return "Echo service factory";
	}

	@Override
	public void updated(String pid, Dictionary<String, ?> properties) throws ConfigurationException {
		System.out.println("wtf " + pid);

		Enumeration<String> enumeration = properties.keys();
		
        while(enumeration.hasMoreElements()) {
            String k = enumeration.nextElement();
            System.out.println(k + ": " + properties.get(k));
        }
		
		if (properties != null) { 
			String portString = properties.get("port").toString();

			try {
				int port = Integer.parseInt(portString);
				System.out.println("Creating echo server on port " + port);

				echoServers.put(pid, new EchoServer(port));
			} catch (Exception e) {
				throw new ConfigurationException("port", "Cannot create a server on port " + portString, e);
			}
		} else if (echoServers.containsKey(pid)) {
			deleted(pid);
		}
	}

	@Override
	public void deleted(String pid) {
		System.out.println("Removing echo server with pid " + pid);
		EchoServer removed = echoServers.remove(pid);
		if (removed != null) {
			removed.stop();
		} 
	}
}