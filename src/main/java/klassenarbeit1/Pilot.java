package klassenarbeit1;

import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;

class Pilot {
  private EV3LargeRegulatedMotor motorLeft;
  private EV3LargeRegulatedMotor motorRight;
  private double TIRE_CIRCUMFERENCE;
  private double WHEEL_SPACING;
  
  Pilot(Port motorPortLeft, Port motorPortRight, double tireDiameter, double wheelSpacing) {
    this.motorLeft = new EV3LargeRegulatedMotor(motorPortLeft);
    this.motorRight = new EV3LargeRegulatedMotor(motorPortRight);
    
    this.TIRE_CIRCUMFERENCE = tireDiameter;
    this.WHEEL_SPACING = wheelSpacing;
  }
  
  public void turnLeft(int angle) {
    this.turnRight(-angle);
  }
  
  public void turnRight(double angle) {
    int individualAngle = this.cmToAngle(Math.PI * WHEEL_SPACING * (angle / 360.0));
  
    this.motorRight.rotate(-individualAngle, true);
    this.motorLeft.rotate(individualAngle, true);
    
    this.motorRight.waitComplete();
    this.motorLeft.waitComplete();
  }
  
  public void backward(int cm) {
    this.forward(-cm);
  }
  
  public void forward(int cm) {
    int angle = this.cmToAngle(cm);
    
    this.motorRight.rotate(angle, true);
    this.motorLeft.rotate(angle, true);
    
    this.motorRight.waitComplete();
    this.motorLeft.waitComplete();
  }
  
  public void stop() {
    this.motorRight.stop();
    this.motorLeft.stop();
  }
  
  private int cmToAngle(double cm) {
    return (int)(cm / this.TIRE_CIRCUMFERENCE * 360.0);
  }
}
