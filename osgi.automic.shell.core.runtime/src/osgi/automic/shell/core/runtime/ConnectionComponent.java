package osgi.automic.shell.core.runtime;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;

import osgi.automic.shell.core.interfaces.IConnectionConfig;

@Component(
    configurationPid = "connection",
    configurationPolicy = ConfigurationPolicy.REQUIRE,
    service=ConnectionComponent.class
)
public class ConnectionComponent {
    @Activate
    void activate(IConnectionConfig config) {
        System.out.println("Connection activated");
        printMessage(config);
    }

    @Modified
    void modified(IConnectionConfig config) {
        System.out.println("Connection modified");
        printMessage(config);
    }

    @Deactivate
    void deactivate() {
        System.out.println("Connection deactivated");
    }

    private void printMessage(IConnectionConfig config) {
    	System.out.println(config.name());
    }
}