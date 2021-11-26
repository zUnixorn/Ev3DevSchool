package twowayrobotconnection.pcserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class DataReceiver extends Thread {
    DatagramSocket robotUdp = new DatagramSocket(8000);
    DatagramPacket latestPacket;
    float ultrasonicValue = 0.0f;

    public DataReceiver() throws SocketException {
    }

    public void run() {
        while (true) {
            try {
                robotUdp.receive(latestPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ultrasonicValue = ByteBuffer.wrap(latestPacket.getData()).getFloat();
        }
    }

    public float getUltrasonicValue() {
        return ultrasonicValue;
    }
}
