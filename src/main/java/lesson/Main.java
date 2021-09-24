package lesson;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Pilot car = new Pilot();

        car.circleRight(30, 180);

        Thread.sleep(10000, 0);
    }
}
