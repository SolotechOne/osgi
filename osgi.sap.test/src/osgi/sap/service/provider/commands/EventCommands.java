package osgi.sap.service.provider.commands;

import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;

import osgi.sap.service.provider.bapi.xbp.xbp;
import osgi.sap.service.provider.bapi.xmi.xmi;

@Component(
		property = {
				CommandProcessor.COMMAND_SCOPE + ":String=event",
				CommandProcessor.COMMAND_FUNCTION + ":String=raise",
				CommandProcessor.COMMAND_FUNCTION + ":String=events",
				CommandProcessor.COMMAND_FUNCTION + ":String=confirm",
				CommandProcessor.COMMAND_FUNCTION + ":String=defined"
		},
		service = EventCommands.class
		)
public class EventCommands {
	private static JCoDestination destination;

	@Activate
	void activate() throws JCoException {
		destination = JCoDestinationManager.getDestination("RK1");

		JCoContext.begin(destination);
	}

	@Deactivate
	void deactivate() throws JCoException {
		JCoContext.end(destination);
	}

	@Descriptor("raise event")
	public void raise(@Descriptor("EventID") String eventid, @Descriptor("Event Parameter") String eventparm) throws JCoException {
		try {
			xmi.bapi_xmi_logon(destination);

			xbp.bapi_xbp_event_raise(destination, eventid, eventparm);
		}
		finally {
			xmi.bapi_xmi_logoff(destination);
		}
	}

	@Descriptor("select events")
	public void events(@Parameter(names={"-s","--state"}, absentValue="N") @Descriptor("State") String state, @Parameter(names={"-c","--confirm"}, absentValue="N", presentValue="C") @Descriptor("Confirm") String confirm, @Descriptor("EventID") String id) throws JCoException {
		try {
			xmi.bapi_xmi_logon(destination);

			xbp.bapi_xbp_btc_evthistory_get(destination, id, state, confirm);
		}
		finally {
			xmi.bapi_xmi_logoff(destination);
		}
	}

	@Descriptor("confirm events")
	public void confirm(@Descriptor("GUID") String guid) throws JCoException {
		try {
			xmi.bapi_xmi_logon(destination);

			xbp.bapi_xbp_btc_evthist_confirm(destination, guid);
		}
		finally {
			xmi.bapi_xmi_logoff(destination);
		}
	}

	@Descriptor("event definitions")
	public void defined(@Parameter(names={"-m","--mask"}, absentValue="%") @Descriptor("Mask") String mask, @Parameter(names={"-f","--from"}, absentValue="0") @Descriptor("From") int from, @Parameter(names={"-t","--to"}, absentValue="0") @Descriptor("To") int to) throws JCoException {
		try {
			xmi.bapi_xmi_logon(destination);

			xbp.bapi_xbp_event_definitions_get(destination, mask, from, to);
		}
		finally {
			xmi.bapi_xmi_logoff(destination);
		}
	}
}