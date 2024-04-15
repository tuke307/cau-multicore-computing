import java.util.*;
import java.lang.*;

import Helper.Result;
import Helper.Functions;


public class evaluation {
    private static final int[] threadCounts = { 1, 2, 4, 6, 8, 10, 12, 14, 16, 32 };
    private static Map<Integer, Result> results = new HashMap<>();

    public static void main(String[] args) {
        calculateAllResults();
        printExecTime();
        printPerformance();
    }

    public static void printExecTime() {
        System.out.println("Thread Count | Execution Time (ms)");
        System.out.println("-------------|-------------------");

        for (int i = 0; i < threadCounts.length; i++) {
            int thread_no = threadCounts[i];
            Result result = results.get(thread_no);

            System.out.printf("%13d | %17d\n", thread_no, result.totalExecutionTime);
        }
    }

    public static void printPerformance() {
        System.out.println("\nThread Count | Performance (1/ms)");
        System.out.println("-------------|-------------------");

        for (int i = 0; i < threadCounts.length; i++) {
            int thread_no = threadCounts[i];
            Result result = results.get(thread_no);

            System.out.printf("%13d | %17f\n", thread_no, 1 / (result.totalExecutionTime / 1000.0));
        }
    }

    public static void calculateAllResults() {
        int[][] a = Functions.readMatrix();
        int[][] b = Functions.readMatrix();

        for (int threadCount : threadCounts) {
            Result result = MatmultD_static.multMatrix(a, b, threadCount);
            results.put(threadCount, result);
        }
    }
}
