package experiment;

public class ControlCodes {
	public final static byte END_CONNECTION = 0x01;
	public final static byte FORWARD = 0x20;
	public final static byte STOP_FORWARD = 0x21;
	public final static byte TURN_RIGHT = 0x22;
	public final static byte STOP_TURN_RIGHT = 0x23;
	public final static byte TURN_LEFT = 0x24;
	public final static byte STOP_TURN_LEFT = 0x25;
	public final static byte BACKWARDS = 0x26;
	public final static byte STOP_BACKWARDS = 0x27;
	public final static byte SPEED_UP = 0x28;
	public final static byte SPEED_DOWN = 0x29;
}