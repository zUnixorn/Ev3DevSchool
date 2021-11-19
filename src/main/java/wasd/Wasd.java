package wasd;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.*;

class Main {
    public static void main(String[] args) throws IOException {
        byte[] IP = {10, (byte)255, (byte)255, (byte)255};
        InetAddress serverIP= InetAddress.getByAddress(IP);
        DatagramSocket serverUDP = new DatagramSocket();

        Pilot car = new Pilot();

        var updSender = new Thread(() -> {
            while (true) {
                byte[] buffer = floatToBytes(car.getDistance());

                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverIP, 8000);

                try {
                    serverUDP.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        updSender.start();

        try (
                ServerSocket serverSocket = new ServerSocket( 8000);
                Socket server = serverSocket.accept();
                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                DataInputStream in = new DataInputStream(server.getInputStream());
        ) {
            while (true) {
                byte key_code = in.readByte();

                if (key_code == -17) {
                    car.forward();
                } else if (key_code == -31) {
                    car.backward();
                } else if (key_code == -30) {
                    car.turnLeft();
                } else if (key_code == -32) {
                    car.turnRight();
                } else if ((key_code == 17) ||
                        (key_code == 31) ||
                        (key_code == 30) ||
                        (key_code == 32)) {
                    car.stop();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static byte[] floatToBytes(float value) {
        int inBits = Float.floatToIntBits(value);
        return new byte[] {(byte) (inBits >> 24),
                (byte) (inBits >> 16),
                (byte) (inBits >> 8),
                (byte) (inBits)};
    }
}
