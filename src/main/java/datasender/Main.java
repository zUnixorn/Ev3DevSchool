package datasender;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

import ev3dev.sensors.ev3.EV3UltrasonicSensor;
import lejos.hardware.port.SensorPort;

public class Main {
    public static void main(String[] args) throws IOException {
        EV3UltrasonicSensor ultrasonicSensor = new EV3UltrasonicSensor(SensorPort.S2);

        float[] samples = new float[ultrasonicSensor.sampleSize()];

        byte[] IP = {10, 42, 0, (byte)167};

        InetAddress plotterIp = InetAddress.getByAddress(IP);

        DatagramSocket plotter = new DatagramSocket();

        //PrintWriter out = new PrintWriter(plotter.getOutputStream(), true);
        long packetNo = 0;

        while (true) {

            ultrasonicSensor.fetchSample(samples, 0);

            System.out.println(samples[0]);

            byte[] buffer = build_packet(samples[0], ++packetNo);

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, plotterIp, 8000);

            plotter.send(packet);
        }
    }

    public static byte[] floatToByteArray(float value) {
        int intBits =  Float.floatToIntBits(value);
        return new byte[] {
                (byte) (intBits >> 24), (byte) (intBits >> 16), (byte) (intBits >> 8), (byte) (intBits) };
    }

    public static byte[] build_packet(float value, long index) {
        int intBits =  Float.floatToIntBits(value);
        return new byte[] {
                (byte) (intBits >> 24), (byte) (intBits >> 16), (byte) (intBits >> 8), (byte) (intBits),
                (byte) (index >> 56), (byte) (index >> 48), (byte) (index >> 40), (byte) (index >> 32),
                (byte) (index >> 24), (byte) (index >> 16), (byte) (index >> 8), (byte) (index) };
    }
}
