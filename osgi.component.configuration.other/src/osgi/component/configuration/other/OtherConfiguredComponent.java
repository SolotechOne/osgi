package osgi.component.configuration.other;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;

@Component(
    configurationPid = "AdminConfiguredComponent",
    configurationPolicy = ConfigurationPolicy.REQUIRE
//    ,service=OtherConfiguredComponent.class
)
public class OtherConfiguredComponent {
    @Activate
    void activate(MessageConfig config) {
        System.out.println("OtherConfiguredComponent activated");
        printMessage(config);
    }

    @Modified
    void modified(MessageConfig config) {
        System.out.println("OtherConfiguredComponent modified");
        printMessage(config);
    }

    @Deactivate
    void deactivate() {
        System.out.println("OtherConfiguredComponent deactivated");
    }

    private void printMessage(MessageConfig config) {
    	String msg = config.message();
        int iter = config.iteration();
 
        for (int i = 1; i <= iter; i++) {
            System.out.println(i + ": " + msg);
        }
    }
}