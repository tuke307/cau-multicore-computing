package proj2.prob1;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import proj2.prob1.ParkingGarage;

class ParkingGarage {
    private final BlockingQueue<Integer> parkingSlots;

    public ParkingGarage(int places) {
        parkingSlots = new ArrayBlockingQueue<>(places);
        for (int i = 0; i < places; i++) {
            try {
                parkingSlots.put(i); // Pre-fill the queue representing free parking spots
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Handle thread interruption
            }
        }
    }

    public void enter(int carNumber) {
        try {
            System.out.println("Car " + carNumber + ": trying to enter");
            parkingSlots.take(); // Block if no spots are available
            System.out.println("Car " + carNumber + ": just entered");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Properly handle interruption
        }
    }

    public void leave(int carNumber) {
        try {
            System.out.println("Car " + carNumber + ": about to leave");
            parkingSlots.put(carNumber); // Release the parking spot back to the pool
            System.out.println("Car " + carNumber + ": have been left");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class ParkingBlockingQueue {
    public static void main(String[] args) {
        ParkingGarage garage = new ParkingGarage(7); // Assume 7 parking places

        // Simulating multiple cars trying to enter and leave the garage
        // 7 cars will enter and park, then each car will leave after 1 second
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
