package experiment;

import ev3dev.actuators.Sound;
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

	public void driveCircle(int circleDiameter) {
		double innerCurveCircumference = ((circleDiameter - Constants.WHEEL_DISTANCE) * Math.PI);
		double outerCurveCircumference = ((circleDiameter + Constants.WHEEL_DISTANCE) * Math.PI);

		double innerCurveAngle = cmToAngle(innerCurveCircumference);
		double outerCurveAngle = cmToAngle(outerCurveCircumference);

		double speedCoEfficient = innerCurveAngle / outerCurveAngle;
		double innerSpeed = Constants.MOTOR_SPEED * (speedCoEfficient);

		motorLeft.setSpeed((int) Math.abs(innerSpeed));
		motorRight.setSpeed(Constants.MOTOR_SPEED);

		motorLeft.rotate((int) Math.round(innerCurveAngle), true);
		motorRight.rotate((int) Math.round(outerCurveAngle), true);

		motorRight.waitComplete();
		motorLeft.waitComplete();

		System.out.println("Circle diameter" + circleDiameter);

		System.out.println("inner curve circumference: " + innerCurveCircumference);
		System.out.println("outer curve circumference: " + outerCurveCircumference);

		System.out.println("inner curve angle: " + innerCurveAngle);
		System.out.println("outer curve angle: " + outerCurveAngle);

		System.out.println("Speed coefficient: " + speedCoEfficient);
		System.out.println("Inner motor speed: " + innerSpeed);



		setSpeed(Constants.MOTOR_SPEED);
	}

	public void turnDegrees(int degrees) {
		setSpeed(Constants.SLOW_MOTOR_SPEED);
		double angleToRotate = cmToAngle((((double) degrees) / 360) * Math.PI * Constants.WHEEL_DISTANCE);

		System.out.println("The angle to rotate is " + angleToRotate);

		motorLeft.rotate((int) angleToRotate * -1, true);
		motorRight.rotate((int) angleToRotate, true);

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
		return (int) ((cm / Constants.WHEEL_CIRCUMFERENCE) * 360);
	}

	private double cmToAngle(double cm) {
		return (cm / Constants.WHEEL_CIRCUMFERENCE) * 360;
	}
}
