package osgi.data.service.online;

import org.osgi.service.component.annotations.Component;

import osgi.data.service.interfaces.DataService;

@Component(
	property = {
		"service.connectivity=online",
		"service.ranking:Integer=7"
	}
)
public class OnlineDataService implements DataService {
	@Override
	public String getData(int id) {
		return "ONLINE data for id " + id;
	}
}