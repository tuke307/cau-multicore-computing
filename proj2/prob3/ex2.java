package proj2.prob3;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ex2 {
    public static void main(String[] args) {
        ReadWriteLock lock = new ReentrantReadWriteLock();
        
        Thread reader = new Thread(() -> {
            lock.readLock().lock();
            try {
                System.out.println("Reading");
            } finally {
                lock.readLock().unlock();
            }
        });

        Thread writer = new Thread(() -> {
            lock.writeLock().lock();
            try {
                System.out.println("Writing");
            } finally {
                lock.writeLock().unlock();
            }
        });

        writer.start();
        reader.start();
    }
}

