import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

class AtomicIntegerReader extends Thread {
    private final AtomicInteger sharedValue;

    public AtomicIntegerReader(AtomicInteger sharedValue) {
        this.sharedValue = sharedValue;
    }

    @Override
    public void run() {
        try {
            System.out.println("Reader " + currentThread().threadId() + ": Read value " + sharedValue.get());
            
            sleep((int) (Math.random() * 1000)); // wait before reading next value
        } catch (InterruptedException e) {
            currentThread().interrupt();
        }
    }
}

class AtomicIntegerWriter extends Thread {
    private final AtomicInteger sharedValue;

    public AtomicIntegerWriter(AtomicInteger sharedValue) {
        this.sharedValue = sharedValue;
    }

    @Override
    public void run() {
        try {
            System.out.println("Writer " + currentThread().threadId() + ": Incremented value to " + sharedValue.getAndIncrement());
            
            sleep((int) (Math.random() * 2000)); // wait before writing next value
        } catch (InterruptedException e) {
            currentThread().interrupt();
        }
    }
}

public class ex3 {
    public static void main(String[] args) throws InterruptedException {
        int numReaders = 3;
        int numWriters = 10;

        if (args.length >= 2) {
            numReaders = Integer.parseInt(args[0]);
            numWriters = Integer.parseInt(args[1]);
        }

        AtomicInteger sharedValue = new AtomicInteger(0);

        System.out.println("AtomicInteger Reader-Writer Simulation");
        System.out.println("Initial value: " + sharedValue.get());
        System.out.println("Number of readers: " + numReaders);
        System.out.println("Number of writers: " + numWriters);

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < numReaders; i++) {
            threads.add(new AtomicIntegerReader(sharedValue));
        }

        for (int i = 0; i < numWriters; i++) {
            threads.add(new AtomicIntegerWriter(sharedValue));
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