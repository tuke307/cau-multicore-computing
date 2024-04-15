package PrimeThreadStrategies;

import Helper.Functions;

public class CyclicPrimeThread extends Thread {
    private int start;
    private int step;
    private int end;
    private int threads;
    private int primeCounter = 0;
    private long executionTime = 0;

    public CyclicPrimeThread(int start, int end, int step, int threads) {
        this.start = start;
        this.step = step;
        this.end = end;
        this.threads = threads;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();

        for (int i = start; i <= end; i += step * threads) {
            for (int j = i; j < i + step && j <= end; j++) {
                if (Functions.isPrime(j)) {
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