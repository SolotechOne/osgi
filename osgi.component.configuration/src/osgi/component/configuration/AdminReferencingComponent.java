package osgi.component.configuration;

import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

@Component
public class AdminReferencingComponent {

//    AdminConfiguredComponent component;
    
    private volatile List<AdminConfiguredComponent> component;
    
    @Activate
    void activate() {
        System.out.println("AdminReferencingComponent activated");
    }

    @Modified
    void modified() {
        System.out.println("AdminReferencingComponent modified");
    }

    @Deactivate
    void deactivate() {
        System.out.println("AdminReferencingComponent deactivated");
    }

    @Reference(
    	cardinality=ReferenceCardinality.MANDATORY,
    	policy=ReferencePolicy.DYNAMIC,
    	policyOption=ReferencePolicyOption.GREEDY
    )
    void setAdminConfiguredComponent(AdminConfiguredComponent comp, Map<String, Object> properties) {
        System.out.println("AdminReferencingComponent: set service");
        printMessage(properties);
    }

    void updatedAdminConfiguredComponent(AdminConfiguredComponent comp, Map<String, Object> properties) {
        System.out.println("AdminReferencingComponent: update service");
        printMessage(properties);
    }

    void unsetAdminConfiguredComponent(AdminConfiguredComponent comp) {
        System.out.println("AdminReferencingComponent: unset service");
    }

    private void printMessage(Map<String, Object> properties) {
        String msg = (String) properties.get("message");
        Integer iter = (Integer) properties.get("iteration");
        System.out.println("[" + msg + "|" + iter + "]");
    }
}