package twowayrobotconnection.robotclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) throws UnknownHostException {
        DataSender dataSender = new DataSender();
        Pilot car = new Pilot();

        dataSender.start();

        try (
                Socket server = new Socket("", 8000);
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
}
