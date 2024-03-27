public class pc_static_cyclic {
    private static int NUM_END = 200000; // default input
    private static int NUM_THREADS = 10;
    private static int counter = 0;

    public static void main(String[] args) {
        if (args.length == 2) {
            NUM_THREADS = Integer.parseInt(args[0]);
            NUM_END = Integer.parseInt(args[1]);
        }
        
        long startTime = System.currentTimeMillis();

        System.out.println("Starting program with " + NUM_THREADS + " threads.");

        Thread[] threads = new Thread[NUM_THREADS];
        int workloadPerThread = NUM_END / NUM_THREADS;

        // For example, assuming 4 threads and 200000 numbers
        // domain decomposition method: {0-49999}, {50000-99999}, {100000-149999}, {150000-199999}
        for (int i = 0; i < NUM_THREADS; i++) {
            final int start = i * workloadPerThread; // 0, 49999, 99999, 149999
            final int end = (i + 1) * workloadPerThread; // 50000, 100000, 150000, 200000

            threads[i] = new Thread(() -> {
                for (int j = start; j < end; j++) {
                    if (isPrime(j)) {
                        synchronized (pc_static_block.class) {
                            counter++;
                        }
                    }
                }
            });

            threads[i].start();
        }

        // Wait for all threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();
        long timeDiff = endTime - startTime;
        System.out.println("Program Execution Time: " + timeDiff + "ms");
        System.out.println("1..." + (NUM_END - 1) + " prime# counter=" + counter);
    }

    private static boolean isPrime(int x) {
        int i;
        if (x <= 1)
            return false;
        for (i = 2; i < x; i++) {
            if (x % i == 0)
                return false;
        }
        return true;
    }
}