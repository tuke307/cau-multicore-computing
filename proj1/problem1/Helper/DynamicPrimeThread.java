package Helper;

import Helper.Functions;
import java.util.concurrent.atomic.AtomicInteger;

public class DynamicPrimeThread extends Thread {
    private int end;
    private AtomicInteger primeCounter = new AtomicInteger(0);
    private long executionTime = 0;
    private AtomicInteger sharedCounter;
    private int taskSize;

    public DynamicPrimeThread(int end, AtomicInteger sharedCounter, int taskSize) {
        this.end = end;
        this.sharedCounter = sharedCounter;
        this.taskSize = taskSize;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();

        while (true) {
            int taskStart, taskEnd;
            synchronized (sharedCounter) {
                taskStart = sharedCounter.get();
                sharedCounter.addAndGet(taskSize);
            }
            if (taskStart >= end)
                break;
            taskEnd = Math.min(taskStart + taskSize, end);
            for (int i = taskStart; i < taskEnd; i++) {
                if (Functions.isPrime(i)) {
                    primeCounter.incrementAndGet();
                }
            }
        }

        long endTime = System.currentTimeMillis();

        executionTime = endTime - startTime;
    }

    public int getCounter() {
        return primeCounter.get();
    }

    public long getExecutionTime() {
        return executionTime;
    }
}