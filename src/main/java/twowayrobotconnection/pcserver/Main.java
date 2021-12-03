package twowayrobotconnection.pcserver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.ConcurrentModificationException;

public class Main {
    public static void main(String[] args) throws IOException {
        ByteArrayOutputStream concat = new ByteArrayOutputStream();
        concat.write("[2J".getBytes(StandardCharsets.UTF_8));
        concat.write(27);
        byte[] clear = concat.toByteArray();

        KeySender keySender = new KeySender();
        DataReceiver dataReceiver = new DataReceiver();
        UltrasonicChart ultrasonicChart = new UltrasonicChart();

        keySender.start();
        dataReceiver.start();
        ultrasonicChart.start();

        while (true) {
            //System.out.write(clear);
            try {
                ultrasonicChart.updateData(dataReceiver.getUltrasonicValue());
            } catch (ConcurrentModificationException ignore) {}
        }
    }
}