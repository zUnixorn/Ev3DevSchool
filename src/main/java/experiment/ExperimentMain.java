package experiment;

import ev3dev.actuators.Sound;
import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import ev3dev.sensors.ev3.EV3UltrasonicSensor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
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

		var sound = Sound.getInstance();

		sound.setVolume(100);
		sound.twoBeeps();
		Delay.msDelay(800);

		var motors = new Pilot(
				motorLeft,
				motorRight,
				Constants.MOTOR_SPEED,
				Constants.SLOW_MOTOR_SPEED,
				Constants.WHEEL_CIRCUMFERENCE,
				Constants.WHEEL_DISTANCE
		);

		var sensor = new UltrasonicSensor(
				new EV3UltrasonicSensor(SensorPort.S3)
		);

		System.out.println("Ready");
		Delay.msDelay(2000);


		int startingTachoCount = motors.getTachoCount();

		motors.driveForward();

		int curveRadius = 15;
		int cmToDrive = 100;
		while (true) {
			int tachoDelta = startingTachoCount - motors.getTachoCount();
			if (tachoDelta >= cmToDrive) {
				break;
			}

			if (sensor.getDistance() < 20) {
				motors.stop();

				motors.driveCentimeters(-15);
				cmToDrive += 15;

				motors.driveCurveDegrees(180, curveRadius);

				cmToDrive += 2 * curveRadius;
			}
			motors.driveForward();
		}

		//Delay.msDelay(3000);

		//System.exit(0);
	}

}
