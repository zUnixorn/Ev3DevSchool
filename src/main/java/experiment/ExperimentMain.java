package experiment;

import ev3dev.actuators.Sound;
import ev3dev.actuators.ev3.EV3Led;
import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import ev3dev.sensors.ev3.EV3UltrasonicSensor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.utility.Delay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;

public class ExperimentMain {

	public static Logger LOGGER = LoggerFactory.getLogger(ExperimentMain.class);

	public static int DELAY = 2000;
	public static final int MOTOR_SPEED = Constants.MOTOR_SPEED;
	public static final int SPEED_INCREMENT = 50;
	static final EV3LargeRegulatedMotor motorLeft = new EV3LargeRegulatedMotor(MotorPort.A);
	static final EV3LargeRegulatedMotor motorRight = new EV3LargeRegulatedMotor(MotorPort.B);

	public static void main(String[] args) {
		LOGGER.info("Starting...");

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("Setting LEDs to yellow");
			var leftLed = new EV3Led(EV3Led.Direction.LEFT);
			var rightLed = new EV3Led(EV3Led.Direction.RIGHT);

			leftLed.setPattern(3);
			rightLed.setPattern(3);
		}));

		motorRight.setSpeed(MOTOR_SPEED);
		motorLeft.setSpeed(MOTOR_SPEED);

		var sound = Sound.getInstance();

		sound.setVolume(100);
		sound.twoBeeps();
		Delay.msDelay(800);

		var motors = new Pilot(
				motorLeft,
				motorRight,
				Constants.MOTOR_SPEED,
				Constants.SLOW_MOTOR_SPEED,
				Constants.WHEEL_CIRCUMFERENCE,
				Constants.WHEEL_DISTANCE
		);

		System.out.println("Ready");
		Delay.msDelay(1000);

		var ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S2);
		var sampleProvider = ultrasonicSensor.getDistanceMode();
		var values = new float[sampleProvider.sampleSize()];

//		var blinkThread = new Thread(() -> {
//			System.out.println("Blinkthread startet executing.");
//			var leftLed = new EV3Led(EV3Led.Direction.LEFT);
//			var rightLed = new EV3Led(EV3Led.Direction.RIGHT);
//
//			// 0 is off
//			// 1 is green
//			// 2 is red
//			// 3 is yellow
//			while (true) {
//				try {
//					long sleepTime = 500;
//					leftLed.setPattern(1);
//					Thread.sleep(sleepTime);
//					leftLed.setPattern(0);
//					rightLed.setPattern(2);
//					Thread.sleep(sleepTime);
//					rightLed.setPattern(0);
//
//					if (Thread.interrupted()) {
//						break;
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});

		var sensorThread = new Thread(() -> {
			while (true) {
				var blinkThread = new BlinkThread();
				try (
						var serverSocket = new ServerSocket(6910);
						var client = serverSocket.accept();
						var out = new DataOutputStream(client.getOutputStream());
				) {
					blinkThread.start(); // Start blinking when sending values
					while (true) {
						sampleProvider.fetchSample(values, 0);
						out.writeFloat(values[0]);
					}
				} catch (Exception ignore) {

				} finally {
					blinkThread.stop();

					System.out.println("Connection got interrupted");
					var leftLed = new EV3Led(EV3Led.Direction.LEFT);
					var rightLed = new EV3Led(EV3Led.Direction.RIGHT);

					leftLed.setPattern(2);
					rightLed.setPattern(0);
				}
			}
		});

		sensorThread.start();


		var motionControlThread = new Thread(() -> {
			while (true) {
				try (
						var serverSocket = new ServerSocket(6900);
						var client = serverSocket.accept();
						var in = new DataInputStream(client.getInputStream());
				) {
					boolean keepAlive = true;
					while (keepAlive) {
						byte instruction = in.readByte();
						switch (instruction) {
							case ControlCodes.END_CONNECTION:
								keepAlive = false;
								break;
							case ControlCodes.FORWARD:
								motors.driveForward();
								break;
							case ControlCodes.TURN_RIGHT:
								motors.turnRight();
								break;
							case ControlCodes.TURN_LEFT:
								motors.turnLeft();
								break;
							case ControlCodes.BACKWARDS:
								motors.driveBackward();
								break;
							case ControlCodes.SPEED_UP:
								motors.speedUp(SPEED_INCREMENT);
								break;
							case ControlCodes.SPEED_DOWN:
								motors.speedDown(SPEED_INCREMENT);
								break;
							// The keyup events all stop the motors, so we catch them here
							case 0x21:
							case 0x23:
							case 0x25:
							case 0x27:
								motors.stop();
								break;

						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		motionControlThread.start();
	}
}
