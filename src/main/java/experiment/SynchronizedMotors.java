package experiment;

import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;

public class SynchronizedMotors {
	private final EV3LargeRegulatedMotor motorLeft;
	private final EV3LargeRegulatedMotor motorRight;

	public SynchronizedMotors(EV3LargeRegulatedMotor motorLeft, EV3LargeRegulatedMotor motorRight) {
		this.motorLeft = motorLeft;
		this.motorRight = motorRight;

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("Emergency Stop");
			motorLeft.stop();
			motorRight.stop();
		}));

		setSpeed(Constants.MOTOR_SPEED);
	}

	public void setSpeed(int speed) {
		motorLeft.setSpeed(speed);
		motorRight.setSpeed(speed);
	}

	public void driveCurve(int curveDiameter) {
		double innerCurveCircumference = ((curveDiameter - (Constants.WHEEL_DIAMETER / 2)) * Math.PI);
		double outerCurveCircumference = ((curveDiameter + (Constants.WHEEL_DIAMETER / 2)) * Math.PI);

		double innerCurveAngle = cmToAngle(innerCurveCircumference);
		double outerCurveAngle = cmToAngle(outerCurveCircumference);

		double speedCoEfficient = innerCurveAngle / outerCurveAngle;
		double innerSpeed = Constants.MOTOR_SPEED * (speedCoEfficient);

		motorLeft.setSpeed((int) innerSpeed);
		motorRight.setSpeed(Constants.MOTOR_SPEED);

		motorLeft.rotate((int) innerCurveAngle, true);
		motorRight.rotate((int) outerCurveAngle, true);

		motorRight.waitComplete();
		motorLeft.waitComplete();

		setSpeed(Constants.MOTOR_SPEED);
	}

	public void turnDegrees(int degrees) {
		setSpeed(Constants.SLOW_MOTOR_SPEED);
		int angleToRotate = cmToAngle((((double) degrees) / 360) * Math.PI * Constants.WHEEL_DISTANCE);

		System.out.println("The angle to rotate is " + angleToRotate);

		motorLeft.rotate(angleToRotate * -1, true);
		motorRight.rotate(angleToRotate, true);

		motorRight.waitComplete();
		setSpeed(Constants.MOTOR_SPEED);
	}

	public void driveCentimeters(int distance) {
		int angleToRotate = cmToAngle(distance);

		motorLeft.rotate(angleToRotate, true);
		motorRight.rotate(angleToRotate, true);
		motorRight.waitComplete();
	}

	private int cmToAngle(int cm) {
		return (int) ((cm / Constants.WHEEL_DIAMETER) * 360);
	}

	private int cmToAngle(double cm) {
		return (int) ((cm / Constants.WHEEL_DIAMETER) * 360);
	}
}
