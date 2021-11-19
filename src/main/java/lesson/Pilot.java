package lesson;

import ev3dev.actuators.Sound;
import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import ev3dev.sensors.ev3.EV3UltrasonicSensor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.SampleProvider;

import java.io.File;

public class Pilot {
    private final EV3UltrasonicSensor ultrasonic = new EV3UltrasonicSensor(SensorPort.S2);
    private final EV3LargeRegulatedMotor motorLeft = new EV3LargeRegulatedMotor(MotorPort.A);
    private final EV3LargeRegulatedMotor motorRight = new EV3LargeRegulatedMotor(MotorPort.B);

    public SampleProvider samples = ultrasonic.getDistanceMode();

    private final int SPEED = 300;
    private final float WHEEL_CIRCUMFERENCE = 17.5f;
    private final float WHEEL_DISTANCE = 11.3f;

    public Pilot() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                System.out.println("Emergency Stop");
                motorLeft.stop();
                motorRight.stop();
            }
        }));

        this.motorRight.setSpeed(this.SPEED);
        this.motorLeft.setSpeed(this.SPEED);

        this.beep();
        this.beep();
    }

    public void forward() {
        this.motorLeft.forward();
        this.motorRight.forward();
    }

    public void forward(double cm) {
        int rotation = cmToAngle(cm);

        this.motorRight.rotate(rotation);
        this.motorLeft.rotate(rotation);
    }

    public void backward() {
        this.motorLeft.backward();
        this.motorRight.backward();
    }

    public void backward(double cm) {
        this.forward(-cm);
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

    private int getInnerAngle(int diameter) {
        return this.cmToAngle((diameter - this.WHEEL_DISTANCE) * Math.PI);
    }

    private int getOuterAngle(int diameter) {
        return this.cmToAngle((diameter + WHEEL_DISTANCE) * Math.PI);
    }

    public void circleRight(int radius) {
        this.circleRight(radius, 360);
    }

    public void circleRight(int radius, int angle) {
        this.circle(radius, angle, true);
    }

    public void circle(int radius, int angle, boolean right) {
        int diameter = radius * 2;

        double fractionOfCircle = (double) angle / 360;

        double rightAngle;
        double leftAngle;

        int rightSpeed = this.SPEED;
        int leftSpeed = this.SPEED;

        if (right) {
            rightAngle = this.getInnerAngle(diameter);
            leftAngle = this.getOuterAngle(diameter);
            rightSpeed = (int) Math.round(Math.abs(rightAngle / leftAngle) * (double) this.SPEED);
        } else {
            leftAngle = this.getInnerAngle(diameter);
            rightAngle = this.getOuterAngle(diameter);
            leftSpeed = (int) Math.round(Math.abs(leftAngle / rightAngle) * (double) this.SPEED);
        }

        this.motorLeft.setSpeed(leftSpeed);
        this.motorRight.setSpeed(rightSpeed);

        this.motorRight.rotate((int) Math.round(rightAngle * fractionOfCircle), true);
        this.motorLeft.rotate((int) Math.round(rightAngle * fractionOfCircle), true);

        this.motorRight.waitComplete();
        this.motorLeft.waitComplete();

        this.motorRight.setSpeed(this.SPEED);
        this.motorLeft.setSpeed(this.SPEED);
    }

    public void circleLeft(int radius) {
        this.circleLeft(radius, 360);
    }

    public void circleLeft(int radius, int angle) {
        this.circle(radius, angle, false);
    }

    public void turnRight(int angle) {
        int rotation = cmToAngle((((double)angle) / 360) * Math.PI * 11.5);

        this.motorRight.rotate(-rotation, true);
        this.motorLeft.rotate(rotation, true);

        this.awaitMotors();
    }

    public void turnLeft(int angle) {
        this.turnRight(-angle);
    }

    public void awaitMotors() {
        this.awaitMotorLeft();
        this.awaitMotorRight();
    }

    public void play(File file) {
        Sound.getInstance().playSample(file);
    }

    public void awaitMotorLeft() {
        this.motorLeft.waitComplete();
    }

    public void awaitMotorRight() {
        this.motorRight.waitComplete();
    }

    public int getDistance() {
        float [] sample = new float[this.samples.sampleSize()];

        this.samples.fetchSample(sample, 0);

        return (int)sample[0];
    }

    int cmToAngle(double cm) {
        return (int)(cm / this.WHEEL_CIRCUMFERENCE * 360);
    }
}
