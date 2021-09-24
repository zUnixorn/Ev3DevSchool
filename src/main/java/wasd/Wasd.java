package wasd;

//import lawnmower.Car;
//import lejos.utility.Delay;

import java.util.Scanner;

public class Wasd {
    public static void main(String[] args) throws InterruptedException {
        //Car car = new Car();

        Scanner stdin = new Scanner(System.in);
        while (true) {
            while (stdin.hasNext()) {
                System.out.println(stdin.nextByte());
            }
        }
    }
}
