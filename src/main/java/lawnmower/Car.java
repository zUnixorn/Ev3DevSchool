package lawnmower;

import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import ev3dev.sensors.ev3.EV3UltrasonicSensor;
import experiment.ExperimentMain;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.SampleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Car {

    public Logger LOGGER = LoggerFactory.getLogger(ExperimentMain.class);

    public EV3UltrasonicSensor ultrasonic = new EV3UltrasonicSensor(SensorPort.S2);
    public EV3LargeRegulatedMotor motorLeft = new EV3LargeRegulatedMotor(MotorPort.A);
    public EV3LargeRegulatedMotor motorRight = new EV3LargeRegulatedMotor(MotorPort.B);

    public SampleProvider samples = ultrasonic.getDistanceMode();

    public Car() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                System.out.println("Emergency Stop");
                motorLeft.stop();
                motorRight.stop();
            }
        }));
    }

    public void forward() {
        this.motorLeft.forward();
        this.motorRight.forward();
    }

    public void backward() {
        this.motorLeft.backward();
        this.motorRight.backward();
    }

    public void stop() {
        this.motorRight.stop();
        this.motorLeft.stop();
    }

    public void turnRight(int angle) {
        int rotation = cmToAngle((((double)angle) / 360) * Math.PI * 11.5);

        this.motorRight.rotate(-rotation, true);
        this.motorLeft.rotate(rotation, true);

        this.awaitMotors();
    }

    public void forwardCm(double cm) {
        int rotation = cmToAngle(cm);

        this.motorRight.rotate(rotation);
        this.motorLeft.rotate(rotation);
    }

    public void awaitMotors() {
        this.awaitMotorLeft();
        this.awaitMotorRight();
    }

    public void awaitMotorLeft() {
        this.motorLeft.waitComplete();
    }

    public void awaitMotorRight() {
        this.motorRight.waitComplete();
    }

    public int checkDistance() {
        float [] sample = new float[this.samples.sampleSize()];

        this.samples.fetchSample(sample, 0);

        return (int)sample[0];
    }

    int cmToAngle(double cm) {
        return (int)(cm / 17.5 * 360);
    }
}
