package osgi.ldap.service.consumer;

import java.util.Map;

import javax.naming.NamingException;

import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.Descriptor;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

import osgi.ldap.service.interfaces.IConnector;
import osgi.ldap.service.provider.LdapConnector;

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
	void setConnector(LdapConnector service, Map<String, Object> properties) {
		this.connector = service;
	}
	
	void unsetConnector(LdapConnector service) {
		if (service == this.connector) {
			this.connector = null;
		}
	}
	
	@Descriptor("search ldap account")
	public void search(String account) {
		try {
			connector.search(account);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
}