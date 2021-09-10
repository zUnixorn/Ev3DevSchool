package lawnmower;

import lejos.utility.Delay;

public class LawnMower {
    public static void main(String[] args) {
        Car lawnmower = new Car();

        while (true) {
            if (lawnmower.checkDistance() < 10) {
                lawnmower.stop();
                lawnmower.turnRight(100);
                lawnmower.forwardCm(-5);
            } else {
                lawnmower.forward();
            }
        }
    }
}
