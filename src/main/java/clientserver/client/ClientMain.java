package clientserver.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClientMain {
	public static void main(String[] args) {
		try (
				Socket server = new Socket("10.42.0.186", 6969);
				DataOutputStream out = new DataOutputStream(server.getOutputStream());
				DataInputStream in = new DataInputStream(server.getInputStream());
		) {
			while(true) {
				System.out.println(in.readFloat());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
