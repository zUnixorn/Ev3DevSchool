package experiment;

import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import ev3dev.sensors.Battery;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExperimentMain {

    public static Logger LOGGER = LoggerFactory.getLogger(ExperimentMain.class);

    public static int DELAY = 2000;

    public static final EV3LargeRegulatedMotor mA = new EV3LargeRegulatedMotor(MotorPort.A);

    public static void main(String[] args) {
        final EV3LargeRegulatedMotor motorLeft = new EV3LargeRegulatedMotor(MotorPort.A);
        final EV3LargeRegulatedMotor motorRight = new EV3LargeRegulatedMotor(MotorPort.B);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                System.out.println("Emergency Stop");
                motorLeft.stop();
                motorRight.stop();
            }
        }));

        System.out.println("Defining the Stop mode");
        motorLeft.brake();
        motorRight.brake();

        System.out.println("Defining motor speed");
        final int motorSpeed = 200;
        motorLeft.setSpeed(motorSpeed);
        motorRight.setSpeed(motorSpeed);

        System.out.println("Go Forward with the motors");
        motorLeft.forward();
        motorRight.forward();

        Delay.msDelay(2000);

        System.out.println("Stop motors");
        motorLeft.stop();
        motorRight.stop();

        System.out.println("Go Backward with the motors");
        motorLeft.backward();
        motorRight.backward();

        Delay.msDelay(2000);

        System.out.println("Stop motors");
        motorLeft.stop();
        motorRight.stop();

        System.out.println("Checking Battery");
        System.out.println("Votage: " + Battery.getInstance().getVoltage());

        System.exit(0);
    }
}
