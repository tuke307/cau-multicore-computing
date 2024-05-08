import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class BlockingQueueProducer extends Thread {
    private final BlockingQueue<Integer> queue;
    private final int itemCount;

    public BlockingQueueProducer(BlockingQueue<Integer> queue, int itemCount) {
        this.queue = queue;
        this.itemCount = itemCount;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= itemCount; i++) {
                queue.put(i);
                System.out.println("Producer: Item " + i + " added to the queue");

                sleep((int) (Math.random() * 1000)); // wait before producing next item
            }
        } catch (InterruptedException e) {
            currentThread().interrupt();
        }
    }
}

class BlockingQueueConsumer extends Thread {
    private final BlockingQueue<Integer> queue;
    private final int itemCount;

    public BlockingQueueConsumer(BlockingQueue<Integer> queue, int itemCount) {
        this.queue = queue;
        this.itemCount = itemCount;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < itemCount; i++) {
                System.out.println("Consumer: Consumed item " + queue.take());

                sleep((int) (Math.random() * 2000)); // wait before consuming next item
            }
        } catch (InterruptedException e) {
            currentThread().interrupt();
        }
    }
}
public class ex1 {
    private static int itemCount = 3;

    public static void main(String[] args) throws InterruptedException {
        if (args.length > 0) {
            try {
                itemCount = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Both arguments must be integers");
                System.exit(1);
            }
        }

        System.out.println("BlockingQueue Producer-Consumer Simulation");
        System.out.println("Number of items: " + itemCount);

        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(itemCount);

        List<Thread> threads = new ArrayList<>();

        threads.add(new BlockingQueueProducer(queue, itemCount));
        threads.add(new BlockingQueueConsumer(queue, itemCount));

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