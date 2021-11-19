package lesson;

import ev3dev.sensors.ev3.EV3TouchSensor;
import lejos.hardware.port.SensorPort;
import lejos.robotics.SampleProvider;

public class TouchSensor {
    private final EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S3);
    private final SampleProvider sampleProvider = touchSensor.getTouchMode();

    public boolean isPressed() {
        float[] samples = new float[sampleProvider.sampleSize()];

        sampleProvider.fetchSample(samples, 0);

        return samples[0] == 1;
    }
}
