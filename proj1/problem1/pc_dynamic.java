public class pc_dynamic {
    private static int num_end = 200000;
    private static int num_threads = 4;
    private static int num_task_size = 10;
    private static int counter = 0;
    private static int sharedCounter = 0;
    private static final Object lock = new Object();                   

    public static void main(String[] args) {
        if (args.length == 3) {
            num_threads = Integer.parseInt(args[0]);
            num_end = Integer.parseInt(args[1]);
            num_task_size = Integer.parseInt(args[2]);
        }

        System.out.println("--dynamic thread handling--");
        System.out.println("using " + num_threads + " threads");

        Result result = calculatePrimes(num_threads, num_end, num_task_size);

        // Print the results
        System.out.println("Total execution time: " + result.totalExecutionTime + "ms");
        System.out.println("Prime counter: " + result.primeCounter);
        for (int i = 0; i < result.threadExecutionTimes.length; i++) {
            System.out.println("Execution time of thread " + (i + 1) + ": " + result.threadExecutionTimes[i] + "ms");
        }
    }

    public static Result calculatePrimes(int num_threads, int num_end, int num_task_size) {
        long startTime = System.currentTimeMillis();
        Thread[] threads = new Thread[num_threads];
        long[] startTimes = new long[num_threads];
        long[] endTimes = new long[num_threads];
        counter = 0;
        sharedCounter = 0;

        for (int i = 0; i < num_threads; i++) {
            final int threadId = i;

            threads[i] = new Thread(() -> {
                startTimes[threadId] = System.currentTimeMillis();
                while (true) {
                    int taskStart, taskEnd;
                    synchronized (lock) {
                        taskStart = sharedCounter;
                        sharedCounter += num_task_size;
                    }
                    if (taskStart >= num_end)
                        break;
                    taskEnd = Math.min(taskStart + num_task_size, num_end);
                    for (int k = taskStart; k < taskEnd; k++) {
                        if (isPrime(k)) {
                            synchronized (lock) {
                                counter++;
                            }
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
        if (x <= 1)
            return false;
        for (int i = 2; i < x; i++) {
            if (x % i == 0)
                return false;
        }
        return true;
    }
}