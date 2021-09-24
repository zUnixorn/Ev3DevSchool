package lawnmower;

import ev3dev.actuators.Sound;
import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import ev3dev.sensors.ev3.EV3UltrasonicSensor;
import experiment.ExperimentMain;
import lejos.hardware.Sounds;
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
    public EV3LargeRegulatedMotor motorTop = new EV3LargeRegulatedMotor(MotorPort.C);

    public SampleProvider samples = ultrasonic.getDistanceMode();

    public float positionMotorTop = 0;

    int lastAngle = 0;
    boolean wayBack = false;

    public int TOP_OFFSET = 90;

    public Car() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                System.out.println("Emergency Stop");
                motorLeft.stop();
                motorRight.stop();
            }
        }));

        this.beep();
        this.beep();

        this.motorTop.setAcceleration(4000);
        this.motorTop.setSpeed(90);
        this.motorTop.rotate(this.TOP_OFFSET);
        this.positionMotorTop = this.motorTop.getPosition();
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

    public void beep() {
        Sound sound = Sound.getInstance();
        sound.setVolume(100);
        sound.beep();
    }

    public void turnRight(int angle) {
        int rotation = cmToAngle((((double)angle) / 360) * Math.PI * 11.5);

        this.motorRight.rotate(-rotation, true);
        this.motorLeft.rotate(rotation, true);

        this.awaitMotors();
    }

//    public void wave(int angle) {
//        this.motorTop.rotate(angle);
//    }

    public void waveNext() {
        System.out.println(this.lastAngle + "");

        if (this.lastAngle >= 60) {
            this.wayBack = true;
        } else if (this.lastAngle <= 0) {
            this.wayBack = false;
        }

        if (this.wayBack) {
            this.motorTop.rotate(-10);
            this.lastAngle -= 10;
        } else {
            this.motorTop.rotate(10);
            this.lastAngle += 10;
        }
//        this.lastAngle = ((this.lastAngle + 10) % 90) - 45;
//        this.motorTop.rotate((int) (this.lastAngle));
    }

    public void setPositionMotorTop() {
        this.positionMotorTop = this.motorTop.getPosition() - TOP_OFFSET;
    }

    public void waveAwait() {
        this.motorTop.waitComplete();
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
