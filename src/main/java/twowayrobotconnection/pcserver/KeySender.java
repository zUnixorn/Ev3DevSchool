package twowayrobotconnection.pcserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class KeySender extends Thread {
    public void run() {
        try (
                ServerSocket serverSocket = new ServerSocket(8000);
                Socket robotTcp = serverSocket.accept();
                DataOutputStream out = new DataOutputStream(robotTcp.getOutputStream());
                DataInputStream in = new DataInputStream(robotTcp.getInputStream());
        ) {
            while (true) {
                out.writeByte(getKeycode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static byte getKeycode() throws IOException {
        int key_code = System.in.read();
        if (key_code > 128) {
            return (byte) (-key_code + 128);
        } else {
            return (byte) key_code;
        }
    }
}
