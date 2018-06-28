package osgi.dependency.manager.sensor;

import osgi.dependency.manager.sensor.interfaces.SensorService;

public class SensorSimulator implements SensorService {
	int temperature = 0;

	public int getMeasurement() {
		int current = temperature;
		
		if (temperature < 500) {
			temperature += 10;
		} else {
			temperature = -100;
		}
		
		return current;
	}
}