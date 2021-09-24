package lawnmower;

import lejos.utility.Delay;

public class LawnMower {
    public static void main(String[] args) throws InterruptedException {
        Car lawnmower = new Car();

//        lawnmower.wave(90);
//        lawnmower.wave(-90);
        while (true) {
            if (lawnmower.checkDistance() < 30) {
                lawnmower.stop();
                lawnmower.turnRight(97);
//                lawnmower.forwardCm(-5);
            } else {
                lawnmower.waveNext();
                lawnmower.forward();
            }
        }
    }
}
