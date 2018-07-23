package osgi.automic.shell.core.service.provider;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.osgi.service.component.annotations.ServiceScope;

import osgi.automic.shell.core.service.interfaces.ISessionConfig;

@Component(
	configurationPid = "session",
	configurationPolicy = ConfigurationPolicy.REQUIRE
	,scope = ServiceScope.PROTOTYPE
	,service = SessionComponent.class
)
public class SessionComponent {
//    @SuppressWarnings("unused")
//	private volatile List<ConnectionComponent> component;
    
    ConnectionComponent connection;
    
    @Activate
    void activate(ISessionConfig config) {
        System.out.println("  session activated " + config.user());
    }

    @Modified
    void modified(ISessionConfig config) {
        System.out.println("  session modified " + config.user());
    }

    @Deactivate
    void deactivate(ISessionConfig config) {
        System.out.println("  session deactivated " + config.user());
    }

    @Reference(
//    	cardinality = ReferenceCardinality.MANDATORY,
    	policy = ReferencePolicy.DYNAMIC,
    	policyOption = ReferencePolicyOption.RELUCTANT
//    	,target = "(service.factoryPid=connection)"
    )
    void setConnectionComponent(ConnectionComponent component, Map<String, Object> properties) {
        System.out.println("  session set connection " + properties.get("name"));
        this.connection = component;
    }

    void updatedConnectionComponent(ConnectionComponent component, Map<String, Object> properties) {
        System.out.println("  session update connection " + properties.get("name"));
    }

    void unsetConnectionComponent(ConnectionComponent component) {
        System.out.println("  session unset connection " + component.toString());
        this.connection = null;
    }
}