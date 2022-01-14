package experiment;

public class SensorServerController implements Runnable {
	private final Thread worker;

	public SensorServerController() {
		worker = new Thread(this, "SensorServerController-Thread");
	}


	@Override
	public void run() {

	}
}
