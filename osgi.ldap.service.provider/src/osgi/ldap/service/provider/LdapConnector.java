package osgi.ldap.service.provider;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import osgi.ldap.service.interfaces.IConnector;

@Component(
	name="osgi.ldap.service.provider.LdapConnector",
	property = {
		"description=declarative service connection for system aetest",
		"LDAPADSERVER=ldap://de.globusgrp.org:3268",
		"LDAPSEARCHBASE=dc=globusgrp,dc=org",
		"LDAPUSERNAME=CN=Carsten Berberich,OU=UsrIntern,OU=UsrAll,OU=1597KooWnd,OU=KOO,DC=de,DC=globusgrp,DC=org",
		"LDAPPASSWORD=Automic13",
		"LDAPACCOUNTTOLOOKUP=Hermann Jochum",
		"SECURITY_AUTHENTICATION=simple",
		"INITIAL_CONTEXT_FACTORY=com.sun.jndi.ldap.LdapCtxFactory"
    },
	service=LdapConnector.class
)
public class LdapConnector implements IConnector {
	@Activate
    void activate(Map<String, Object> properties) {
		System.out.println(properties.get("LDAPADSERVER"));
	}
	
	@Override
	public void search(String account) {
		System.out.println("ldap: searching for account " + account);
	}
}