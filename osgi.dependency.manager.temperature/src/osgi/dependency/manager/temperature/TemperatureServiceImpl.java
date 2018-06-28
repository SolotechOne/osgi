package osgi.dependency.manager.temperature;

import osgi.dependency.manager.sensor.interfaces.SensorService;
import osgi.dependency.manager.temperature.interfaces.TemperatureService;

public class TemperatureServiceImpl implements TemperatureService {

    // Injected by DependencyManager, see Activator.init()
    private volatile SensorService sensor;

    public int getTemperature() {
        int measuredValue = sensor.getMeasurement();
        int degreesCelcius = (measuredValue / 10) - 20;
        return degreesCelcius;
    }
}