package erste_klausur;

import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;

public class Pilot {
  private EV3LargeRegulatedMotor motorLeft;
  private EV3LargeRegulatedMotor motorRight;
  private double wheelDiameter;
  private double wheelTrack;
  private int defaultSpeed;
  private double wheelCircumference;
  
  public Pilot(Port motorLeft, Port motorRight, double wheelDiameter, double wheelTrack, int defaultSpeed) {
    this.motorLeft = new EV3LargeRegulatedMotor(motorLeft);
    this.motorRight = new EV3LargeRegulatedMotor(motorRight);
    this.wheelDiameter = wheelDiameter;
    this.wheelTrack = wheelTrack;
    this.setSpeed(this.defaultSpeed);
    this.wheelCircumference = this.wheelDiameter * Math.PI;
    }
  
  public void driveCentimeters(double centimeters) {
    double angle = this.cmToAngle(centimeters);
    System.err.println("Angle: " + angle);
    this.motorLeft.rotate((int) angle, true);
    System.err.println("After left rotate");
    this.motorRight.rotate((int) angle, true);
    System.err.println("After right rotate");
    this.waitComplete();
    System.err.println("After wait complete");
  }
  
  public void driveCurve(double degrees, double radius, boolean driveLeft) {
    double outerCircle = 2 * (radius + (this.wheelTrack / 2)) * Math.PI;
    double innerCircle = 2 * (radius - (this.wheelTrack / 2)) * Math.PI;
    
    double circleSpeedCoefficient = innerCircle / outerCircle;
    double speedInnerCircle = this.defaultSpeed * circleSpeedCoefficient;
    
    double circleAmount = degrees / 360;
    
    double outerCircleAngles = cmToAngle(outerCircle * circleAmount);
    double innerCircleAngles = cmToAngle(innerCircle * circleAmount);
    
    if (driveLeft) {
      this.motorLeft.setSpeed((int) speedInnerCircle);
      this.motorLeft.rotate((int) innerCircleAngles, true);
      this.motorRight.rotate((int) outerCircleAngles, true);
    } else {
      this.motorRight.setSpeed((int) speedInnerCircle);
      this.motorRight.rotate((int) innerCircleAngles, true);
      this.motorLeft.rotate((int) outerCircleAngles, true);
    }
    
    this.waitComplete();
  }
  
  public void turn(int degreesToRotate, boolean turnLeft) {
    double angleToRotate = ((double) degreesToRotate / 360) * this.wheelTrack * Math.PI;

    if (turnLeft) {
      this.motorLeft.rotate((int) (-1 * angleToRotate), true);
      this.motorRight.rotate((int) angleToRotate, true);
    } else {
      this.motorLeft.rotate((int) angleToRotate, true);
      this.motorRight.rotate((int) (-1 * angleToRotate), true);
    }
    this.waitComplete();
  }

  public void setSpeed(int speed) {
    this.motorLeft.setSpeed(speed);
    this.motorRight.setSpeed(speed);
  }
  
  private void waitComplete() {
    this.motorLeft.waitComplete();
    this.motorRight.waitComplete();
  }  

  private double cmToAngle(double cm) {
    return (cm / this.wheelCircumference) * 360;    
  }

  
}

