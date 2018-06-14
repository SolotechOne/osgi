package osgi.ldap.service.consumer;

import java.util.Map;

import org.apache.felix.service.command.CommandProcessor;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

import osgi.ldap.service.interfaces.IConnector;

@Component(
	property= {
		CommandProcessor.COMMAND_SCOPE + "=ldap",
		CommandProcessor.COMMAND_FUNCTION + "=search"},
	service=SearchCommand.class
)
public class SearchCommand {
	private IConnector connector;

	@Reference(
		policy=ReferencePolicy.DYNAMIC,
		policyOption=ReferencePolicyOption.GREEDY,
		target="(component.name=osgi.ldap.service.provider.LdapConnector)"
	)
	void setDataService(IConnector service, Map<String, Object> properties) {
		this.connector = service;
	}
	
	void unsetDataService(IConnector service) {
		if (service == this.connector) {
			this.connector = null;
		}
	}
	
	public void open(String account) {
		connector.search(account);
	}
}