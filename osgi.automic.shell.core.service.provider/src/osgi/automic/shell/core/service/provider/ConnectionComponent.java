package osgi.automic.shell.core.service.provider;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;

import osgi.automic.shell.core.service.interfaces.IConnectionConfig;

@Component(
    configurationPid = "connection",
    configurationPolicy = ConfigurationPolicy.REQUIRE,
    service = ConnectionComponent.class
)
public class ConnectionComponent {
    @Activate
    void activate(IConnectionConfig config) {
        System.out.println("    connection activated " + config.name());
//        printMessage(config);
    }

    @Modified
    void modified(IConnectionConfig config) {
        System.out.println("    connection modified " + config.name());
//        printMessage(config);
    }

    @Deactivate
    void deactivate() {
        System.out.println("    connection deactivated");
    }

//    private void printMessage(IConnectionConfig config) {
//    	System.out.println(config.name());
//    }
}