package lesson;

import java.io.File;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Pilot car = new Pilot();
        TouchSensor sensor = new TouchSensor();

        File nyanCat = new File("resources/nyan_cat.wav");

        car.play(nyanCat);

        while (true) {
            if (sensor.isPressed()) {
                car.stop();
                car.forward(-3);
                car.turnRight(90);
                car.circleLeft(10, 180);
                car.turnRight(90);
            } else {
                car.forward();
            }
        }
    }
}
