import Helper.Functions;
import Helper.Result;
import Helper.CyclicPrimeThread;

public class pc_static_cyclic {
    private static int num_end = 200000;
    private static int num_threads = 4;
    private static int num_task_size = 10;

    public static void main(String[] args) {
        if (args.length == 3) {
            num_threads = Integer.parseInt(args[0]);
            num_end = Integer.parseInt(args[1]);
            num_task_size = Integer.parseInt(args[2]);
        }

        System.out.println("--static cyclic thread handling--");
        System.out.println("using " + num_threads + " threads");

        Result result = calculatePrimes(num_threads, num_end, num_task_size);

        System.out.println("Program Execution Time: " + result.totalExecutionTime + "ms");
        System.out.println("1..." + (num_end - 1) + " prime# counter=" + result.primeCounter);
        for (int i = 0; i < result.threadExecutionTimes.length; i++) {
            System.out.println("Execution time of thread " + (i + 1) + ": " + result.threadExecutionTimes[i] + "ms");
        }
    }

    public static Result calculatePrimes(int num_threads, int num_end, int num_task_size) {
        long startTime = System.currentTimeMillis();
        CyclicPrimeThread[] threads = new CyclicPrimeThread[num_threads];

        // for example, assuming 4 threads and 200000 numbers, with task size of 10
        // numbers
        // {1~10, 41~50, 81~90, ...} {11~20, 51~60, 91~100, ...}, {21~30, 61~70,
        // 101~110, ...}, {31~40, 71~80, 111~120, ...}
        for (int i = 0; i < num_threads; i++) {
            threads[i] = new CyclicPrimeThread(i * num_task_size + 1, num_end, num_task_size, num_threads);
            threads[i].start();
        }

        // Wait for all threads to finish
        for (CyclicPrimeThread thread : threads) {
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