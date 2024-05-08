import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class ParkingGarage {
    private final BlockingQueue<Integer> parkingSlots;

    public ParkingGarage(int places) {
        parkingSlots = new ArrayBlockingQueue<Integer>(places);

        for (int i = 0; i < places; i++) {
            try {
                parkingSlots.put(i);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void enter() {
        try {
            parkingSlots.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void leave() {
        try {
            parkingSlots.put(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Car extends Thread {
    private final ParkingGarage parkingGarage;

    public Car(String name, ParkingGarage p) {
        super(name);
        this.parkingGarage = p;
        start();
    }

    private void tryingEnter() {
        System.out.println(getName() + ": trying to enter");
    }

    private void justEntered() {
        System.out.println(getName() + ": just entered");
    }

    private void aboutToLeave() {
        System.out.println(getName() + ":                                     about to leave");
    }

    private void left() {
        System.out.println(getName() + ":                                     have been left");
    }

    public void run() {
        while (true) {
            try {
                sleep((int) (Math.random() * 10000)); // drive before parking
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            tryingEnter();
            parkingGarage.enter();
            justEntered();

            try {
                sleep((int) (Math.random() * 20000)); // stay within the parking garage
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            aboutToLeave();
            parkingGarage.leave();
            left();
        }
    }
}

public class ParkingBlockingQueue {
    private static int parkingPlaces = 7;
    private static int carNumber = 10;

    public static void main(String[] args) {
        if (args.length > 0) {
            try {
                parkingPlaces = Integer.parseInt(args[0]);
                carNumber = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Argument must be an integer");
                System.exit(1);
            }
        }

        System.out.println("Parking Garage Simulation using BlockingQueue");
        System.out.println("Number of parking places: " + parkingPlaces);
        System.out.println("Number of cars: " + carNumber);

        ParkingGarage garage = new ParkingGarage(parkingPlaces);

        // Simulating multiple cars trying to enter and leave the garage
        for (int i = 1; i <= carNumber; i++) {
            new Car("Car " + i, garage);
        }
    }
}