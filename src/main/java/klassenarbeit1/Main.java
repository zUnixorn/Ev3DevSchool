package klassenarbeit1;

import lejos.hardware.port.MotorPort;


class Main {
  public static void main(String[] args) {
    Pilot car = new Pilot(MotorPort.A, MotorPort.B, 17.5, 12.5);

    int track = 185;
    
    int rest = track % 40;
    int iterations = (int)(track / 40);
    
    for (int i = 0; i < (iterations - 1); i++) {
      car.forward(40);
      car.turnLeft(90);
      car.forward(20);
      car.backward(20);
      car.turnRight(90);
    } // end of for
    car.forward(rest);
    
    car.turnRight(180);
    car.forward(track);
  }
}
