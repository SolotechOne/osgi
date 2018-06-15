package osgi.ldap.service.interfaces;

import javax.naming.NamingException;

public interface IConnector {
	public void search(String account) throws NamingException;
}