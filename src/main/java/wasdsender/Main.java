package wasdsender;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        try (
                Socket server = new Socket("10.42.0.115", 8000);
                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                DataInputStream in = new DataInputStream(server.getInputStream());
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
