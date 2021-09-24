package experiment;

import ev3dev.actuators.Sound;
import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExperimentMain {

	public static Logger LOGGER = LoggerFactory.getLogger(ExperimentMain.class);

	public static int DELAY = 2000;
	public static final int MOTOR_SPEED = Constants.MOTOR_SPEED;
	static final EV3LargeRegulatedMotor motorLeft = new EV3LargeRegulatedMotor(MotorPort.A);
	static final EV3LargeRegulatedMotor motorRight = new EV3LargeRegulatedMotor(MotorPort.B);

	public static void main(String[] args) {
		LOGGER.info("Starting...");

		motorRight.setSpeed(MOTOR_SPEED);
		motorLeft.setSpeed(MOTOR_SPEED);

//		var sound = Sound.getInstance();
//
//		sound.setVolume(100);
//		sound.twoBeeps();
		Delay.msDelay(800);

		LOGGER.debug("test");
		var motors = new SynchronizedMotors(motorLeft, motorRight);

		motors.driveCircle(50);

		Delay.msDelay(3000);

		System.exit(0);
	}

	public static void driveCircle(SynchronizedMotors motors) {

	}

	public static void initializeMotors() {
		motorLeft.setSpeed(MOTOR_SPEED);
		motorRight.setSpeed(MOTOR_SPEED);
	}

	public static void turnDegrees(int degrees) {
		motorRight.rotate(degrees, true);
		motorLeft.rotate(degrees * -1, true);
	}

	public static void driveCentimeters(int distance) {
		motorLeft.rotate((int) ((distance / Constants.WHEEL_CIRCUMFERENCE) * 360), true);
		motorRight.rotate((int) ((distance / Constants.WHEEL_CIRCUMFERENCE) * 360), true);

		motorRight.waitComplete();
	}
}
