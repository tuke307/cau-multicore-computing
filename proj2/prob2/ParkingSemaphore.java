package proj2.prob2;

import java.util.concurrent.Semaphore;

class ParkingGarage {
    private final Semaphore semaphore;

    public ParkingGarage(int places) {
        // Initialize the semaphore with the number of available places
        semaphore = new Semaphore(places);
    }

    public void enter(int carNumber) {
        try {
            System.out.println("Car " + carNumber + ": trying to enter");
            semaphore.acquire(); // Attempt to acquire a permit to park, blocks if none available
            System.out.println("Car " + carNumber + ": just entered");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Properly handle interruption
        }
    }

    public void leave(int carNumber) {
        System.out.println("Car " + carNumber + ": about to leave");
        semaphore.release(); // Release the permit, allowing another car to park
        System.out.println("Car " + carNumber + ": have been left");
    }
}

public class ParkingSemaphore {
    public static void main(String[] args) {
        ParkingGarage garage = new ParkingGarage(7); // Assume 7 parking places

        // Simulating multiple cars trying to enter and leave the garage
        for (int i = 1; i <= 10; i++) {
            final int carNumber = i;
            new Thread(() -> garage.enter(carNumber)).start();
            new Thread(() -> {
                try {
                    Thread.sleep(1000); // Simulating the duration a car remains parked
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                garage.leave(carNumber);
            }).start();
        }
    }
}
