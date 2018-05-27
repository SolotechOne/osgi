package osgi.connection.implementation;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;

import osgi.connection.interfaces.IConnection;

@Component(
	service=ConnectionDS.class,
	name="osgi.connection.implementation.Connection",
	property = {
		"description=declarative service connection for system aetest",
		"servername=s1597taeap001.koogrp.globus.net",
		"port=2211"
	    }
)
public class ConnectionDS implements IConnection{
    @Activate
    void activate() {
        System.out.println("ConnectionDS activated");
    }

    @Modified
    void modified() {
        System.out.println("ConnectionDS modified");
    }

    @Deactivate
        void deactivate() {
        System.out.println("ConnectionDS deactivated");
    }

	@Override
	public void open(String servername, int port) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void login(int client, String user, String department, String password, char language) {
		// TODO Auto-generated method stub
		
	}
}