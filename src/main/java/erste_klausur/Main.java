package erste_klausur;

import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import lejos.hardware.port.*;

public class Main {
  private Pilot pilot;
  public static void main(String[] args) {
    Pilot pilot = new Pilot(
          MotorPort.A,
          MotorPort.B,
          6,
          10.0,
          200
    );
    
    //driving the snowplow
    System.err.println("Starting");
    int distanceToDrive = 185; //Distance in cm
    int distanceToDriveBeforeCurving = 40;
    for (int i = 0; i < distanceToDrive / distanceToDriveBeforeCurving ; i++) {
      System.err.println("Loop run");
      pilot.driveCentimeters(distanceToDriveBeforeCurving);
      System.err.println("After 1");
      pilot.driveCurve(90, 7.5, true);
      System.err.println("After 2");
      pilot.driveCurve(-90, 7.5, true);
      System.err.println("After 3");
    }
    
    //Drive the rest of the distance
    pilot.driveCentimeters(distanceToDrive % distanceToDriveBeforeCurving);
    
    //Turn around and drive back
    pilot.turn(180, true);
    pilot.driveCentimeters(distanceToDrive);
  }
}
