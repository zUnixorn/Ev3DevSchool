package twowayrobotconnection.robotclient;

import java.io.IOException;
import java.net.*;

public class DataSender extends Thread {
    UltrasonicSensor ultrasonicSensor = new UltrasonicSensor();
    InetAddress serverIp = InetAddress.getByAddress(new byte[] {(byte) 10,(byte) 42,(byte) 0, (byte) 167});
    DatagramSocket server;

    public DataSender() throws UnknownHostException {
    }

    public void run() {
        try {
            server = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        while (true) {
            float distance = ultrasonicSensor.getDistance();

            System.out.println(distance);

            byte[] buffer = floatToByteArray(distance);

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, this.serverIp, 8000);

            try {
                server.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static byte[] floatToByteArray(float value) {
        int intBits =  Float.floatToIntBits(value);
        return new byte[] { (byte) (intBits >> 24), (byte) (intBits >> 16), (byte) (intBits >> 8), (byte) (intBits) };
    }
}
