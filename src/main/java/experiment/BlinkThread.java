package experiment;

import ev3dev.actuators.ev3.EV3Led;

import java.util.concurrent.atomic.AtomicBoolean;

public class BlinkThread implements Runnable {
	private final Thread worker;
	private final AtomicBoolean running = new AtomicBoolean();

	public BlinkThread() {
		worker = new Thread(this);
	}

	public void start() {
		worker.start();
	}

	public void stop() {
		this.running.set(false);
	}

	@Override
	public void run() {
		this.running.set(true);
		System.out.println("Blinkthread startet executing.");
		var leftLed = new EV3Led(EV3Led.Direction.LEFT);
		var rightLed = new EV3Led(EV3Led.Direction.RIGHT);

		// 0 is off
		// 1 is green
		// 2 is red
		// 3 is yellow
		while (this.running.get()) {
			try {
				leftLed.setPattern(0);
				rightLed.setPattern(0);

				long sleepTime = 50;
				leftLed.setPattern(1);
				Thread.sleep(sleepTime);
				leftLed.setPattern(0);
			} catch (Exception ignore) {

			}
		}
		rightLed.setPattern(2);
	}
}
