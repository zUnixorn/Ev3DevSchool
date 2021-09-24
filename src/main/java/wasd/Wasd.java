package wasd;

import java.io.IOException;


public class Wasd {
    public static void main(String[] args) throws IOException {
        Pilot car = new Pilot();

        char key;

        while (true) {
            if ((System.in.available() != 0)) {
                key = (char) System.in.read();
            } else {
                key = 0;
            }

            switch (System.in.read()) {
                case 'w':
                    car.forward();
                case 's':
                    car.backward();
                case 'c':
                    System.exit(0);
                case 0:
                    car.stop();
            }
        }
    }
}
