package osgi.automic.shell.core.commands;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import org.apache.felix.service.command.Descriptor;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
		property= {
				"osgi.command.scope:String=data",
				"osgi.command.function:String=ranking"
		},
		service=RankingCommand.class
	)
public class RankingCommand {

	ConfigurationAdmin admin;

	@Reference
	void setConfigurationAdmin(ConfigurationAdmin admin) {
		this.admin = admin;
	}

	@Descriptor("fuck")
	public void ranking() throws IOException, InvalidSyntaxException {
		String filter = "(service.factoryPid=connection)";
		
		Configuration[] configurations = this.admin.listConfigurations(filter);
		
		for (Configuration configuration : configurations) {
//			String service_name = (String) configuration.getProperties().get("name");
//			String service_pid = (String) configuration.getProperties().get("service.pid");
//			String service_ranking = (String) configuration.getProperties().get("service.ranking");
//			
//			Integer ranking = Integer.parseInt(service_ranking);
//			
////			System.out.println(active + " " + service_name);
//			
//			System.out.println(
//				service_name + " (" + 
//				service_pid + ") " + 
//				ranking
//			);
			
			Dictionary<String, Object> propsOnline = null;
			
			if (configuration != null && configuration.getProperties() != null) {
				propsOnline = configuration.getProperties();
			} else {
				propsOnline = new Hashtable<>();
			}
			
			int onlineRanking = 7;
			
			if (configuration != null && configuration.getProperties() != null) {
				Object rank = configuration.getProperties().get("service.ranking");
				
				if (rank != null) {
//					System.out.println(rank);
					onlineRanking = (Integer)rank;
				}
			}
	
			// toggle between 3 and 7
			onlineRanking = (onlineRanking == 7) ? 3 : 7;
	
			propsOnline.put("service.ranking", onlineRanking);
			
			configuration.update(propsOnline);
			
		}
		
//		Configuration configOnline = this.admin.getConfiguration( "osgi.data.service.online.OnlineDataService", null);
//		
//		Dictionary<String, Object> propsOnline = null;
//		
//		if (configOnline != null && configOnline.getProperties() != null) {
//			propsOnline = configOnline.getProperties();
//		} else {
//			propsOnline = new Hashtable<>();
//		}
//
//		int onlineRanking = 7;
//		
//		if (configOnline != null && configOnline.getProperties() != null) {
//			Object rank = configOnline.getProperties().get("service.ranking");
//			
//			if (rank != null) {
//				onlineRanking = (Integer)rank;
//			}
//		}
//
//		// toggle between 3 and 7
//		onlineRanking = (onlineRanking == 7) ? 3 : 7;
//
//		propsOnline.put("service.ranking", onlineRanking);
//		
//		configOnline.update(propsOnline);
	}
}