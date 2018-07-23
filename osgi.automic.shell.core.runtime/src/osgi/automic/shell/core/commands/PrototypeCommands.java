package osgi.automic.shell.core.commands;

import java.util.Map;

import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.Descriptor;
import org.osgi.service.component.ComponentServiceObjects;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceScope;

import osgi.automic.shell.core.service.provider.SessionComponent;

@Component(
	property = {
		CommandProcessor.COMMAND_SCOPE + ":String=session",
		CommandProcessor.COMMAND_FUNCTION + ":String=proto"
	},
	service = PrototypeCommands.class
)
public class PrototypeCommands {
//	SessionComponent session;
	
	private ComponentServiceObjects<SessionComponent> factory;
	
	@Reference(
//		policy = ReferencePolicy.STATIC,
//		policyOption = ReferencePolicyOption.GREEDY
//		,
		scope = ReferenceScope.PROTOTYPE_REQUIRED
	)
	void setSessionComponent(ComponentServiceObjects<SessionComponent> component, Map<String, Object> properties) {
		System.out.println("proto: set factory " + properties.get("user"));
		this.factory = component;
	}

	void updatedSessionComponent(ComponentServiceObjects<SessionComponent> component, Map<String, Object> properties) {
		System.out.println("proto: update factory " + properties.get("user"));
	}

	void unsetSessionComponent(ComponentServiceObjects<SessionComponent> component) {
		System.out.println("proto: unset factory " + component.toString());
		this.factory = null;
	}
	    
	@Descriptor("prototype command")
    public void proto() {    
		// create a new service instance
		SessionComponent session = factory.getService();
        
        try {
    		System.out.println("proto executed " + session);
        } finally {
            // destroy the service instance
            factory.ungetService(session);
        }
    }
}