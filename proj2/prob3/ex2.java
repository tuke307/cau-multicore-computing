import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class ReadWriteLockReader extends Thread {
    private final ReadWriteLock lock;
    private final int[] sharedValue;

    public ReadWriteLockReader(ReadWriteLock lock, int[] sharedValue) {
        this.lock = lock;
        this.sharedValue = sharedValue;
    }

    @Override
    public void run() {
        lock.readLock().lock();
        try {
            System.out.println("Reader " + currentThread().threadId() + ": Read value " + sharedValue[0]);
            
            sleep((int) (Math.random() * 1000)); // wait before reading next value
        } catch (InterruptedException e) {
            currentThread().interrupt();
        } finally {
            lock.readLock().unlock();
        }
    }
}

class ReadWriteLockWriter extends Thread {
    private final ReadWriteLock lock;
    private final int[] sharedValue;

    public ReadWriteLockWriter(ReadWriteLock lock, int[] sharedValue) {
        this.lock = lock;
        this.sharedValue = sharedValue;
    }

    @Override
    public void run() {
        lock.writeLock().lock();
        try {
            sharedValue[0]++;
            System.out.println("Writer " + currentThread().threadId() + ": Incremented value to " + sharedValue[0]);
            
            sleep((int) (Math.random() * 2000)); // wait before writing next value
        } catch (InterruptedException e) {
            currentThread().interrupt();
        } finally {
            lock.writeLock().unlock();
        }
    }
}

public class ex2 {
    public static void main(String[] args) throws InterruptedException {
        int numReaders = 3;
        int numWriters = 10;

        if (args.length >= 2) {
            numReaders = Integer.parseInt(args[0]);
            numWriters = Integer.parseInt(args[1]);
        }

        ReadWriteLock lock = new ReentrantReadWriteLock();
        int[] sharedValue = {0};

        System.out.println("ReadWriteLock Reader-Writer Simulation");
        System.out.println("Initial value: " + sharedValue[0]);
        System.out.println("Number of readers: " + numReaders);
        System.out.println("Number of writers: " + numWriters);

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < numReaders; i++) {
            threads.add(new ReadWriteLockReader(lock, sharedValue));
        }

        for (int i = 0; i < numWriters; i++) {
            threads.add(new ReadWriteLockWriter(lock, sharedValue));
        }

        // mix the threads, so that readers and writers are interleaved
        Collections.shuffle(threads);

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
    }
}