package osgi.automic.shell.core.runtime;

import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

@Component(
	configurationPid = "session",
	configurationPolicy = ConfigurationPolicy.REQUIRE
)
public class SessionComponent {
    @SuppressWarnings("unused")
	private volatile List<ConnectionComponent> component;
    
    @Activate
    void activate() {
        System.out.println("Session activated");
    }

    @Modified
    void modified() {
        System.out.println("Session modified");
    }

    @Deactivate
    void deactivate() {
        System.out.println("Session deactivated");
    }

    @Reference(
    	cardinality=ReferenceCardinality.MANDATORY,
    	policy=ReferencePolicy.DYNAMIC,
    	policyOption=ReferencePolicyOption.GREEDY
//    	,target="(name=aeprd)"
    )
    void setAdminConfiguredComponent(ConnectionComponent component, Map<String, Object> properties) {
        System.out.println("Session: set connection " + properties.get("name"));
    }

    void updatedAdminConfiguredComponent(ConnectionComponent component, Map<String, Object> properties) {
        System.out.println("Session: update connection " + properties.get("name"));
    }

    void unsetAdminConfiguredComponent(ConnectionComponent component) {
        System.out.println("Session: unset connection " + component.toString());
    }
}