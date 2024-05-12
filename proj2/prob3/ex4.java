import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

class CyclicBarrierReader extends Thread {
    private final CyclicBarrier barrier;
    private final AtomicInteger sharedValue;

    public CyclicBarrierReader(CyclicBarrier barrier, AtomicInteger sharedValue) {
        this.barrier = barrier;
        this.sharedValue = sharedValue;
    }

    @Override
    public void run() {
        try {
            System.out.println("Reader " + currentThread().threadId() + ": waiting at barrier");
            
            barrier.await();
            
            // Read the shared value after crossing the barrier
            System.out.println("Reader " + currentThread().threadId() + ": " + sharedValue.get());
        } catch (Exception e) {
            currentThread().interrupt();
        }
    }
}

class CyclicBarrierWriter extends Thread {
    private final CyclicBarrier barrier;
    private final AtomicInteger sharedValue;

    public CyclicBarrierWriter(CyclicBarrier barrier, AtomicInteger sharedValue) {
        this.barrier = barrier;
        this.sharedValue = sharedValue;
    }

    @Override
    public void run() {
        try {
            System.out.println("Writer " + Thread.currentThread().threadId() + ": waiting at barrier");
            
            barrier.await();

            // Increment the shared value after crossing the barrier
            sharedValue.incrementAndGet();
        } catch (Exception e) {
            currentThread().interrupt();
        }
    }
}

public class ex4 {
    public static void main(String[] args) throws InterruptedException {
        int numReaders = 3;
        int numWriters = 10;

        if (args.length >= 2) {
            numReaders = Integer.parseInt(args[0]);
            numWriters = Integer.parseInt(args[1]);
        }

        CyclicBarrier barrier = new CyclicBarrier(numReaders + numWriters, () -> System.out.println("Barrier reached"));

        AtomicInteger sharedValue = new AtomicInteger(0);

        System.out.println("CyclicBarrier Reader-Writer Simulation");
        System.out.println("Number of readers: " + numReaders);
        System.out.println("Number of writers: " + numWriters);
        System.out.println("initial shared value: " + sharedValue.get());

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < numReaders; i++) {
            threads.add(new CyclicBarrierReader(barrier, sharedValue));
        }

        for (int i = 0; i < numWriters; i++) {
            threads.add(new CyclicBarrierWriter(barrier, sharedValue));
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