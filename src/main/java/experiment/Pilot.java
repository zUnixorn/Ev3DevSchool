package experiment;

import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;

public class Pilot {
	private final EV3LargeRegulatedMotor motorLeft;
	private final EV3LargeRegulatedMotor motorRight;
	private final int motor_speed;
	private final int slow_motor_speed;
	private final double wheel_circumference;
	private final double wheel_distance;

	public Pilot(EV3LargeRegulatedMotor motorLeft, EV3LargeRegulatedMotor motorRight, int motorSpeed, int slowMotorSpeed, double wheelCircumference, double wheelDistance) {
		this.motorLeft = motorLeft;
		this.motorRight = motorRight;
		this.motor_speed = motorSpeed;
		this.slow_motor_speed = slowMotorSpeed;
		this.wheel_circumference = wheelCircumference;
		this.wheel_distance = wheelDistance;

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("Emergency Stop");
			motorLeft.stop();
			motorRight.stop();
		}));

		setSpeed(motorSpeed);
	}

	public Pilot(EV3LargeRegulatedMotor motorLeft, EV3LargeRegulatedMotor motorRight) {
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

	public void driveCurveDegrees(int degrees, int radius) {
		driveCurveDegrees(degrees, radius, true);
	}

	public void driveCurveDegrees(int degrees, int radius, boolean driveLeft) {
		double partOfCircle = (double) degrees / 360;

		double innerCurveCircumference = (2 * (radius - (wheel_distance / 2)) * Math.PI);
		double outerCurveCircumference = (2 * (radius + (wheel_distance / 2)) * Math.PI);

		double innerCurveAngle = cmToAngle(innerCurveCircumference);
		double outerCurveAngle = cmToAngle(outerCurveCircumference);

		double speedCoEfficient = innerCurveAngle / outerCurveAngle;
		double innerSpeed = motor_speed * (speedCoEfficient);

		if (driveLeft) {
			motorLeft.setSpeed((int) Math.abs(innerSpeed));
			motorRight.setSpeed(motor_speed);


			motorLeft.rotate((int) Math.round(partOfCircle * innerCurveAngle), true);
			motorRight.rotate((int) Math.round(partOfCircle * outerCurveAngle), true);
		} else {
			motorRight.setSpeed((int) Math.abs(innerSpeed));
			motorLeft.setSpeed(motor_speed);

			motorRight.rotate((int) Math.round(partOfCircle * innerCurveAngle), true);
			motorLeft.rotate((int) Math.round(partOfCircle * outerCurveAngle), true);
		}


		motorRight.waitComplete();
		motorLeft.waitComplete();

//		System.out.println("inner curve circumference: " + innerCurveCircumference);
//		System.out.println("outer curve circumference: " + outerCurveCircumference);
//
//		System.out.println("inner curve angle: " + innerCurveAngle);
//		System.out.println("outer curve angle: " + outerCurveAngle);
//
//		System.out.println("Speed coefficient: " + speedCoEfficient);
//		System.out.println("Inner motor speed: " + innerSpeed);
//
//		System.out.println("Circle degrees part factor: " + partOfCircle);
//
//		System.out.println("inner circle half: " + Math.round(partOfCircle * innerCurveAngle));
//		System.out.println("outer circle half: " + Math.round(partOfCircle * outerCurveAngle));

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

	//Exists only to fulfill the tasks for school
	public void driveCircle(int circleRadius, boolean driveLeft) {
		driveCurveDegrees(360, circleRadius, driveLeft);
	}

	public void driveCircle(int circleRadius) {
		driveCircle(circleRadius, true);
	}

	public void driveForward() {
		motorLeft.forward();
		motorLeft.forward();
	}

	public void stop() {
		motorLeft.stop();
		motorRight.stop();
	}
}
