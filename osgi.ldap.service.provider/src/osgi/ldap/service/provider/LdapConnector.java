package osgi.ldap.service.provider;

import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

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
		".LDAPPASSWORD=Automic13",
//		"LDAPACCOUNTTOLOOKUP=Hermann Jochum",
		"SECURITY_AUTHENTICATION=simple",
		"INITIAL_CONTEXT_FACTORY=com.sun.jndi.ldap.LdapCtxFactory"
    },
	service=LdapConnector.class
)
public class LdapConnector implements IConnector {
	private String LDAPADSERVER;
	private String LDAPSEARCHBASE;
	private String LDAPUSERNAME;
	private String LDAPPASSWORD;
	private String SECURITY_AUTHENTICATION;
	private String INITIAL_CONTEXT_FACTORY;
	
	@Activate
    void activate(Map<String, Object> properties) {
//		System.out.println(properties.get("LDAPADSERVER"));
		
		this.LDAPADSERVER = (String) properties.get("LDAPADSERVER");
		this.LDAPSEARCHBASE = (String) properties.get("LDAPSEARCHBASE");
		this.LDAPUSERNAME = (String) properties.get("LDAPUSERNAME");
		this.LDAPPASSWORD = (String) properties.get(".LDAPPASSWORD");
		this.SECURITY_AUTHENTICATION = (String) properties.get("SECURITY_AUTHENTICATION");
		this.INITIAL_CONTEXT_FACTORY = (String) properties.get("INITIAL_CONTEXT_FACTORY");
	}
	
	@Override
	public void search(String account) throws NamingException {
		Properties properties = new Properties();
		properties.put(Context.SECURITY_AUTHENTICATION, this.SECURITY_AUTHENTICATION);
		properties.put(Context.SECURITY_PRINCIPAL, this.LDAPUSERNAME);         
		properties.put(Context.SECURITY_CREDENTIALS, this.LDAPPASSWORD);
		properties.put(Context.INITIAL_CONTEXT_FACTORY, this.INITIAL_CONTEXT_FACTORY);
        properties.put(Context.PROVIDER_URL, this.LDAPADSERVER);
        
//        properties.put("com.sun.jndi.ldap.trace.ber", System.err);	//debug
        
        LdapContext context = new InitialLdapContext(properties, null);
        
        String searchFilter = "(&(objectClass=user)(|(Name=" + account + ")))";
//        String searchFilter = "(&(objectCategory=person)(objectClass=user)(|(Name=" + LDAPACCOUNTTOLOOKUP + ")(Name=Hermann Jochum)))";
        
        
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        searchControls.setReturningAttributes(new String[]{"displayName", "mail", "telephoneNumber"});
//        searchControls.setCountLimit(2);
        
        NamingEnumeration<SearchResult> results = context.search(this.LDAPSEARCHBASE, searchFilter, searchControls);
        
        while(results.hasMoreElements()) {
        	SearchResult searchResult = (SearchResult) results.nextElement();
        	
//        	NamingEnumeration attributes = searchResult.getAttributes().getAll();
        	
//        	for (NamingEnumeration ae = searchResult.getAttributes().getAll(); ae.hasMore();) {
//                Attribute attr = (Attribute) ae.next();
//                System.out.println("attribute: " + attr.getID());
//
//                /* print each value */
//                for (NamingEnumeration e = attr.getAll(); e.hasMore(); System.out.println("value: " + e.next()));
//        	}
        	
//            String name = (String)searchResult.getAttributes().get("name").get();
            String displayName = (String)searchResult.getAttributes().get("displayName").get();
            String mail = (String)searchResult.getAttributes().get("mail").get();
            String phone = (String)searchResult.getAttributes().get("telephoneNumber").get();
            
            System.out.println(displayName + "(" + mail + ") " + phone);
        }
	}
}