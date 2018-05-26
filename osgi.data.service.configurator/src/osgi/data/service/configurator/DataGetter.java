package osgi.data.service.configurator;

import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

import osgi.data.service.interfaces.DataService;

@Component(
	property= {
			"osgi.command.scope:String=data",
			"osgi.command.function:String=get"
	},
	service=DataGetter.class
)
public class DataGetter {

	private DataService dataService;

	@Reference(
		policy=ReferencePolicy.DYNAMIC,
		policyOption=ReferencePolicyOption.GREEDY
	)
	void setDataService(DataService service, Map<String, Object> properties) {
		this.dataService = service;
	}

	void unsetDataService(DataService service) {
		if (service == this.dataService) {
			this.dataService = null;
		}
	}

	public void get(int id) {
		System.out.println(this.dataService.getData(id));
	}
}