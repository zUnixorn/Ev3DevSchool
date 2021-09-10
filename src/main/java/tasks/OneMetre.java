package tasks;

import ev3dev.actuators.lego.motors.Motor;
import ev3dev.sensors.ev3.EV3UltrasonicSensor;
import lejos.hardware.port.SensorPort;
import lejos.robotics.SampleProvider;

public class OneMetre {
    public static void main(String[] args) {
        int angle = cmToAngle(100);

        Motor.A.rotate(angle, true);
        Motor.B.rotate(angle, true);

        Motor.B.waitComplete();
        Motor.A.waitComplete();
    }

    public static int cmToAngle(int cm) {
        return (int)(cm / 17.5 * 360);
    }
}
