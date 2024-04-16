package PrimeThreadStrategies;

import Helper.Functions;

public class CyclicPrimeThread extends Thread {
    private int step;
    private int end;
    private int threads;
    private int id;
    private int primeCounter = 0;
    private long executionTime = 0;

    public CyclicPrimeThread(int end, int step, int threads, int id) {
        this.step = step;
        this.end = end;
        this.threads = threads;
        this.id = id;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();

        // outer loop: to divide the task into smaller tasks
        for (int start = id * step; start <= end; start += threads * step) {
            int taskEnd = Math.min(start + step, end);

            // inner loop: to check for prime numbers
            for (int currentNumber = start; currentNumber < taskEnd; currentNumber++) {
                if (Functions.isPrime(currentNumber)) {
                    primeCounter++;
                }
            }
        }

        long endTime = System.currentTimeMillis();

        executionTime = endTime - startTime;
    }

    public int getCounter() {
        return primeCounter;
    }

    public long getExecutionTime() {
        return executionTime;
    }
}