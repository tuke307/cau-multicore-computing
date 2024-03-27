public class pc_static_block {
    private static int num_end = 200000; // default input
    private static int num_threads = 4;
    private static int counter = 0;

    public static void main(String[] args) {
        if (args.length == 2) {
            num_threads = Integer.parseInt(args[0]);
            num_end = Integer.parseInt(args[1]);
        }

        System.out.println("--static block thread handling--");
        System.out.println("using " + num_threads + " threads");

        Result result = calculatePrimes(num_threads, num_end);

        System.out.println("Program Execution Time: " + result.totalExecutionTime + "ms");
        System.out.println("1..." + (num_end - 1) + " prime# counter=" + result.primeCounter);
        for (int i = 0; i < result.threadExecutionTimes.length; i++) {
            System.out.println("Execution time of thread " + (i+1) + ": " + result.threadExecutionTimes[i] + "ms");
        }
    }

    public static Result calculatePrimes(int num_threads, int num_end) {
        long startTime = System.currentTimeMillis();
        Thread[] threads = new Thread[num_threads];
        long[] startTimes = new long[num_threads];
        long[] endTimes = new long[num_threads];
        int workloadPerThread = num_end / num_threads;
        counter = 0;

        // for example, assuming 4 threads and 200000 numbers
        // static decomposition method: {0-49999}, {50000-99999}, {100000-149999}, {150000-199999}
        for (int i = 0; i < num_threads; i++) {
            final int start = i * workloadPerThread; // 0, 49999, 99999, 149999
            final int end = (i + 1) * workloadPerThread; // 50000, 100000, 150000, 200000
            final int threadId = i;

            threads[i] = new Thread(() -> {
                startTimes[threadId] = System.currentTimeMillis();
                for (int j = start; j < end; j++) {
                    if (isPrime(j)) {
                        synchronized (pc_static_block.class) {
                            counter++;
                        }
                    }
                }
                endTimes[threadId] = System.currentTimeMillis();
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
        long totalExecutionTime = endTime - startTime;
        long[] threadExecutionTimes = new long[num_threads];
        for (int i = 0; i < num_threads; i++) {
            threadExecutionTimes[i] = endTimes[i] - startTimes[i];
        }

        return new Result(totalExecutionTime, threadExecutionTimes, counter);
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