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
                Socket server = new Socket("10.42.0.167", 8000);
                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                DataInputStream in = new DataInputStream(server.getInputStream());
        ) {
            while (true) {
                byte key_code = in.readByte();

                switch (key_code) {
                    case -17:
                        car.forward();
                        break;
                    case -31:
                        car.backward();
                        break;
                    case -30:
                        car.turnLeft();
                        break;
                    case -32:
                        car.turnRight();
                        break;
                    case 17:
                    case 31:
                    case 30:
                    case 32:
                        car.stop();
                        break;
                    default:
                        break;
                }

//                if (key_code == -17) {
//                    car.forward();
//                } else if (key_code == -31) {
//                    car.backward();
//                } else if (key_code == -30) {
//                    car.turnLeft();
//                } else if (key_code == -32) {
//                    car.turnRight();
//                } else if ((key_code == 17) ||
//                        (key_code == 31) ||
//                        (key_code == 30) ||
//                        (key_code == 32)) {
//                    car.stop();
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
