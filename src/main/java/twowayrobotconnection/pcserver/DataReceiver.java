package twowayrobotconnection.pcserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class DataReceiver extends Thread {
    byte[] buffer = new byte[4];

    DatagramSocket robotUdp = new DatagramSocket(8000);
    DatagramPacket latestPacket = new DatagramPacket(buffer, 4);
    float ultrasonicValue = 0.0f;

    public DataReceiver() throws SocketException {
    }

    public void run() {
        while (true) {
            try {
                robotUdp.receive(latestPacket);
                ultrasonicValue = ByteBuffer.wrap(latestPacket.getData()).getFloat();
            } catch (IOException ignore) {}
        }
    }

    public float getUltrasonicValue() {
        return ultrasonicValue;
    }
}
