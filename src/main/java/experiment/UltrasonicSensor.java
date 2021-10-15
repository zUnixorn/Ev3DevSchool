package experiment;

import ev3dev.sensors.ev3.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class UltrasonicSensor {
	private final EV3UltrasonicSensor ultrasonicSensor;
	private final SampleProvider sampleProvider;

	public UltrasonicSensor(EV3UltrasonicSensor ultrasonicSensor) {
		this.ultrasonicSensor = ultrasonicSensor;
		this.sampleProvider = this.ultrasonicSensor.getDistanceMode();
	}

	//Gets current distance reading from the ultrasonic sensor
	public int getDistance() {
		float [] sample = new float[this.sampleProvider.sampleSize()];

		this.sampleProvider.fetchSample(sample, 0);

		return (int)sample[0];
	}
}
