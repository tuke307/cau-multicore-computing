import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

class CyclicBarrierReader extends Thread {
    private final CyclicBarrier barrier;

    public CyclicBarrierReader(CyclicBarrier barrier) {
        this.barrier = barrier;
    }

    @Override
    public void run() {
        try {
            System.out.println("Reader " + currentThread().threadId() + ": waiting at barrier");
            sleep((int) (Math.random() * 1000)); // wait before reaching the barrier
            barrier.await();
        } catch (Exception e) {
            currentThread().interrupt();
        }
    }
}

class CyclicBarrierWriter extends Thread {
    private final CyclicBarrier barrier;

    public CyclicBarrierWriter(CyclicBarrier barrier) {
        this.barrier = barrier;
    }

    @Override
    public void run() {
        try {
            System.out.println("Writer " + Thread.currentThread().threadId() + ": waiting at barrier");
            sleep((int) (Math.random() * 2000)); // wait before reaching the barrier
            barrier.await();
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

        System.out.println("CyclicBarrier Reader-Writer Simulation");
        System.out.println("Number of readers: " + numReaders);
        System.out.println("Number of writers: " + numWriters);

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < numReaders; i++) {
            threads.add(new CyclicBarrierReader(barrier));
        }

        for (int i = 0; i < numWriters; i++) {
            threads.add(new CyclicBarrierWriter(barrier));
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