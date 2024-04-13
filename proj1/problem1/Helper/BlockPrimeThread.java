package Helper;

import Helper.Functions;

public class BlockPrimeThread extends Thread {
    private int start;
    private int end;
    private int primeCounter = 0;
    private long executionTime = 0;

    public BlockPrimeThread(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();

        for (int i = start; i <= end; i++) {
            if (Functions.isPrime(i)) {
                primeCounter++;
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