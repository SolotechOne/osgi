package osgi.component.configuration;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

@Component(
    property = {
        "message=Welcome to the inline configured service",
        "iteration:Integer=3"
    }
)
public class StaticConfiguredComponent {
    @Activate
    void activate(Map<String, Object> properties) {
        String msg = (String) properties.get("message"); //$NON-NLS-1$
        Integer iter = (Integer) properties.get("iteration"); //$NON-NLS-1$

        for (int i = 1; i <= iter; i++) {
            System.out.println(i + ": " + msg); //$NON-NLS-1$
        }
        
        System.out.println();
    }
}