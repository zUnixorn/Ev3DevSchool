package tasks;

import ev3dev.sensors.ev3.EV3UltrasonicSensor;
import lejos.hardware.port.SensorPort;
import ev3dev.actuators.lego.motors.Motor;
import ev3dev.sensors.ev3.EV3UltrasonicSensor;
import lejos.hardware.port.SensorPort;
import lejos.robotics.SampleProvider;

public class Ultrasonic {

    public static EV3UltrasonicSensor ultrasonic = new EV3UltrasonicSensor(SensorPort.S2);

    public static void main(String[] args) {
        final SampleProvider samples = ultrasonic.getDistanceMode();

        int distance = 11;

        Motor.A.forward();
        Motor.B.forward();

        while (distance > 10) {
            float [] sample = new float[samples.sampleSize()];

            samples.fetchSample(sample, 0);

            distance = (int)sample[0];
        }

        Motor.A.stop();
        Motor.B.stop();
    }
}
