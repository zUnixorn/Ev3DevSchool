package experiment;

import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;

public class SynchronizedMotors {
	private final EV3LargeRegulatedMotor motorLeft;
	private final EV3LargeRegulatedMotor motorRight;
	private final int motor_speed;
	private final int slow_motor_speed;
	private final double wheel_circumference;
	private final double wheel_distance;

	public SynchronizedMotors(EV3LargeRegulatedMotor motorLeft, EV3LargeRegulatedMotor motorRight, int motor_speed, int slow_motor_speed, double wheel_circumference, double wheel_distance) {
		this.motorLeft = motorLeft;
		this.motorRight = motorRight;
		this.motor_speed = motor_speed;
		this.slow_motor_speed = slow_motor_speed;
		this.wheel_circumference = wheel_circumference;
		this.wheel_distance = wheel_distance;

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("Emergency Stop");
			motorLeft.stop();
			motorRight.stop();
		}));

		setSpeed(motor_speed);
	}

	public SynchronizedMotors(EV3LargeRegulatedMotor motorLeft, EV3LargeRegulatedMotor motorRight) {
		this(
				motorLeft,
				motorRight,
				Constants.MOTOR_SPEED,
				Constants.SLOW_MOTOR_SPEED,
				Constants.WHEEL_CIRCUMFERENCE,
				Constants.WHEEL_DISTANCE
		);
	}


	public void setSpeed(int speed) {
		motorLeft.setSpeed(speed);
		motorRight.setSpeed(speed);
	}

	public void driveCircle(int circleDiameter) {
		double innerCurveCircumference = ((circleDiameter - wheel_distance) * Math.PI);
		double outerCurveCircumference = ((circleDiameter + wheel_distance) * Math.PI);

		double innerCurveAngle = cmToAngle(innerCurveCircumference);
		double outerCurveAngle = cmToAngle(outerCurveCircumference);

		double speedCoEfficient = innerCurveAngle / outerCurveAngle;
		double innerSpeed = motor_speed * (speedCoEfficient);

		motorLeft.setSpeed((int) Math.abs(innerSpeed));
		motorRight.setSpeed(motor_speed);

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


		setSpeed(motor_speed);
	}

	public void turnDegrees(int degrees) {
		setSpeed(slow_motor_speed);
		double angleToRotate = cmToAngle((((double) degrees) / 360) * Math.PI * wheel_distance);

		System.out.println("The angle to rotate is " + angleToRotate);

		motorLeft.rotate((int) angleToRotate * -1, true);
		motorRight.rotate((int) angleToRotate, true);

		motorRight.waitComplete();
		setSpeed(motor_speed);
	}

	public void driveCentimeters(int distance) {
		int angleToRotate = cmToAngle(distance);

		motorLeft.rotate(angleToRotate, true);
		motorRight.rotate(angleToRotate, true);
		motorRight.waitComplete();
	}

	private int cmToAngle(int cm) {
		return (int) ((cm / wheel_circumference) * 360);
	}

	private double cmToAngle(double cm) {
		return (cm / wheel_circumference) * 360;
	}
}
