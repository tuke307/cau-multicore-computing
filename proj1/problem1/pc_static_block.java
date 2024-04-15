import Helper.Functions;
import Helper.Result;
import PrimeThreadStrategies.BlockPrimeThread;


public class pc_static_block {
    private static int num_end = 200000;
    private static int num_threads = 4;

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
        BlockPrimeThread[] threads = new BlockPrimeThread[num_threads];
        int workloadPerThread = num_end / num_threads;

        // for example, assuming 4 threads and 200000 numbers
        // static decomposition method: {0-49999}, {50000-99999}, {100000-149999}, {150000-199999}
        for (int i = 0; i < num_threads; i++) {
            final int start = i * workloadPerThread;
            final int end = (i + 1) * workloadPerThread;

            threads[i] = new BlockPrimeThread(start, end);
            threads[i].start();
        }

        // Wait for all threads to finish
        for (BlockPrimeThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();
        long totalExecutionTime = endTime - startTime;
        long[] threadExecutionTimes = new long[num_threads];
        int totalCounter = 0;
        for (int i = 0; i < num_threads; i++) {
            threadExecutionTimes[i] = threads[i].getExecutionTime();
            totalCounter += threads[i].getCounter();
        }

        return new Result(totalExecutionTime, threadExecutionTimes, totalCounter);
    }
}