
import java.util.HashMap;
import java.util.Map;
import Helper.Result;

public class evaluation {
    private static int NUM_END = 200000;
    private static int NUM_TASK_SIZE = 10;
    private static final int[] THREAD_COUNTS = { 1, 2, 4, 6, 8, 10, 12, 14, 16, 32 };
    private static Map<String, Map<Integer, Result>> results = new HashMap<>();

    public static void main(String[] args) {
        if (args.length == 2) {
            NUM_END = Integer.parseInt(args[0]);
            NUM_TASK_SIZE = Integer.parseInt(args[1]);
        }

        calculateAllResults();
        printExecTime();
        printPerformance();
    }

    private static Result calculatePrimesByType(String type, int threadCount) {
        switch (type) {
            case "static (block)":
                return pc_static_block.calculatePrimes(threadCount, NUM_END);
            case "static (cyclic)":
                return pc_static_cyclic.calculatePrimes(threadCount, NUM_END, NUM_TASK_SIZE);
            case "dynamic":
                return pc_dynamic.calculatePrimes(threadCount, NUM_END, NUM_TASK_SIZE);
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }
    }

    private static void calculateAllResults() {
        for (String type : new String[] { "static (block)", "static (cyclic)", "dynamic" }) {
            results.put(type, new HashMap<>());
            for (int threadCount : THREAD_COUNTS) {
                Result result = calculatePrimesByType(type, threadCount);
                results.get(type).put(threadCount, result);
            }
        }
    }

    private static void printExecTime() {
        System.out.println();
        System.out.println("Execution time (ms)");

        // Print the table header
        System.out.printf("%-15s", "threads");
        for (String column : new String[] { "static (block)", "primeCount", "static (cyclic)", "primeCount", "dynamic",
                "primeCount" }) {
            System.out.printf("%-15s", column);
        }
        System.out.println();

        // Print the results for each thread count
        for (int threadCount : THREAD_COUNTS) {
            System.out.printf("%-15d", threadCount);
            for (String type : new String[] { "static (block)", "static (cyclic)", "dynamic" }) {
                Result result = results.get(type).get(threadCount);
                System.out.printf("%-15d", result.totalExecutionTime);
                System.out.printf("%-15d", result.primeCounter);
            }
            System.out.println();
        }
    }

    private static void printPerformance() {
        System.out.println();
        System.out.println("Performance (1/ms)");

        // Print the table header
        System.out.printf("%-15s", "threads");
        for (String type : new String[] { "static (block)", "static (cyclic)", "dynamic" }) {
            System.out.printf("%-15s", type);
        }
        System.out.println();

        // Print the results for each thread count
        for (int threadCount : THREAD_COUNTS) {
            System.out.printf("%-15d", threadCount);
            for (String type : new String[] { "static (block)", "static (cyclic)", "dynamic" }) {
                Result result = results.get(type).get(threadCount);
                System.out.printf("%-15f", (1.0 / result.totalExecutionTime));
            }
            System.out.println();
        }
    }
}