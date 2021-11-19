package experiment;

import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;

public class Pilot {
	private final EV3LargeRegulatedMotor motorLeft;
	private final EV3LargeRegulatedMotor motorRight;
	private final int motorSpeed;
	private final int slowMotorSpeed;
	private final double wheelCircumference;
	private final double wheelDistance;

	public Pilot(EV3LargeRegulatedMotor motorLeft, EV3LargeRegulatedMotor motorRight, int motorSpeed, int slowMotorSpeed, double wheelCircumference, double wheelDistance) {
		this.motorLeft = motorLeft;
		this.motorRight = motorRight;
		this.motorSpeed = motorSpeed;
		this.slowMotorSpeed = slowMotorSpeed;
		this.wheelCircumference = wheelCircumference;
		this.wheelDistance = wheelDistance;

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
		driveCurveDegrees(degrees, radius, true, false);
	}

	public void driveCurveDegrees(int degrees, int radius, boolean driveLeft) {
		driveCurveDegrees(degrees, radius, driveLeft, false);
	}

	public void driveCurveDegrees(int degrees, int radius, boolean driveLeft, boolean immediateReturn) {
		double partOfCircle = (double) degrees / 360;

		double innerCurveCircumference = (2 * (radius - (wheelDistance / 2)) * Math.PI);
		double outerCurveCircumference = (2 * (radius + (wheelDistance / 2)) * Math.PI);

		double innerCurveAngle = cmToAngle(innerCurveCircumference);
		double outerCurveAngle = cmToAngle(outerCurveCircumference);

		double speedCoEfficient = innerCurveAngle / outerCurveAngle;
		double innerSpeed = motorSpeed * (speedCoEfficient);

		if (driveLeft) {
			motorLeft.setSpeed((int) Math.abs(innerSpeed));
			motorRight.setSpeed(motorSpeed);


			motorLeft.rotate((int) Math.round(partOfCircle * innerCurveAngle), true);
			motorRight.rotate((int) Math.round(partOfCircle * outerCurveAngle), true);
		} else {
			motorRight.setSpeed((int) Math.abs(innerSpeed));
			motorLeft.setSpeed(motorSpeed);

			motorRight.rotate((int) Math.round(partOfCircle * innerCurveAngle), true);
			motorLeft.rotate((int) Math.round(partOfCircle * outerCurveAngle), true);
		}


		if(!immediateReturn) {
			waitMotorsComplete();
		}
	}

	public void turnDegrees(int degrees) {
		turnDegrees(degrees, false);
	}

	public void turnDegrees(int degrees, boolean immediateReturn) {
		setSpeed(slowMotorSpeed);
		double angleToRotate = cmToAngle((((double) degrees) / 360) * Math.PI * wheelDistance);

		motorLeft.rotate((int) angleToRotate * -1, true);
		motorRight.rotate((int) angleToRotate, true);

		if (!immediateReturn) {
			waitMotorsComplete();
		}
		setSpeed(motorSpeed);
	}

	public void driveCentimeters(int distance) {
		driveCentimeters(distance, false);
	}

	public void driveCentimeters(int distance, boolean immediateReturn) {
		int angleToRotate = cmToAngle(distance);

		motorLeft.rotate(angleToRotate, true);
		motorRight.rotate(angleToRotate, true);

		if (!immediateReturn) {
			waitMotorsComplete();
		}
	}

	public int getTachoCount(boolean left) {
		if (left) {
			return motorLeft.getTachoCount();
		}
		return motorRight.getTachoCount();
	}
	private int cmToAngle(int cm) {
		return (int) ((cm / wheelCircumference) * 360);
	}

	private double cmToAngle(double cm) {
		return (cm / wheelCircumference) * 360;
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
		motorRight.forward();
	}

	public void driveBackward() {
		motorLeft.backward();
		motorRight.backward();
	}

	public void stop() {
		motorLeft.stop();
		motorRight.stop();
	}

	private void waitMotorsComplete() {
		this.motorRight.waitComplete();
		this.motorLeft.waitComplete();
	}
}