package experiment;

import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;

public class SynchronizedMotors {
	private final EV3LargeRegulatedMotor motorLeft;
	private final EV3LargeRegulatedMotor motorRight;

	public SynchronizedMotors(EV3LargeRegulatedMotor motorLeft, EV3LargeRegulatedMotor motorRight) {
		this.motorLeft = motorLeft;
		this.motorRight = motorRight;
	}

	public void setSpeed(int speed) {
		motorLeft.setSpeed(speed);
		motorRight.setSpeed(speed);
	}

	public void turnDegrees(int degrees) {
		setSpeed(Constants.SLOW_MOTOR_SPEED);

		motorLeft.rotate(degrees * -1, true);
		motorRight.rotate(degrees, true);
		motorRight.waitComplete();

		setSpeed(Constants.MOTOR_SPEED);
	}

	public void driveCentimeters(int distance) {
		motorLeft.rotate((int) ((distance / Constants.WHEEL_DIAMETER) * 360), true);
		motorRight.rotate((int) ((distance / Constants.WHEEL_DIAMETER) * 360), true);
		motorRight.waitComplete();
	}
}
