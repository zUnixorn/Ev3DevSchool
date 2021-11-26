package twowayrobotconnection.robotclient;

import ev3dev.sensors.ev3.EV3UltrasonicSensor;
import lejos.hardware.port.SensorPort;
import lejos.robotics.SampleProvider;

public class UltrasonicSensor {
    private final EV3UltrasonicSensor ultrasonic = new EV3UltrasonicSensor(SensorPort.S1);
    public SampleProvider samples = ultrasonic.getDistanceMode();

    public float getDistance() {
        float [] sample = new float[this.samples.sampleSize()];

        this.samples.fetchSample(sample, 0);

        return sample[0];
    }
}
