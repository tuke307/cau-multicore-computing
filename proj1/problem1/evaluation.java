import java.util.HashMap;
import java.util.Map;

public class evaluation {
    private static int num_end = 200000;
    private static int num_task_size = 10;
    private static int[] threadCounts = {1, 2, 4, 6, 8, 10, 12, 14, 16, 32};
    private static Map<String, Map<Integer, Result>> results = new HashMap<>();

    public static void main(String[] args) {
        calculateAllResults();
        print_exec_time();
        System.out.println();
        print_performance();
    }

    private static void calculateAllResults() {
        for (String type : new String[]{"static (block)", "static (cyclic)", "dynamic"}) {
            results.put(type, new HashMap<>());
            for (int threadCount : threadCounts) {
                Result result;
                switch (type) {
                    case "static (block)":
                        result = pc_static_block.calculatePrimes(threadCount, num_end);
                        break;
                    case "static (cyclic)":
                        result = pc_static_cyclic.calculatePrimes(threadCount, num_end, num_task_size);
                        break;
                    case "dynamic":
                        result = pc_dynamic.calculatePrimes(threadCount, num_end, num_task_size);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid type: " + type);
                }
                results.get(type).put(threadCount, result);
            }
        }
    }

    private static void print_exec_time() {
        // Print the table header
        System.out.printf("%-15s", "exec (ms)");
        for (String type : new String[]{"static (block)", "static (cyclic)", "dynamic"}) {
            System.out.printf("%-15s", type);
        }
        System.out.println();

        // Print the results for each thread count
        for (int threadCount : threadCounts) {
            System.out.printf("%-15d", threadCount);
            for (String type : new String[]{"static (block)", "static (cyclic)", "dynamic"}) {
                Result result = results.get(type).get(threadCount);
                System.out.printf("%-15d", result.totalExecutionTime);
            }
            System.out.println();
        }
    }

    private static void print_performance() {
        // Print the table header
        System.out.printf("%-15s", "perf");
        for (String type : new String[]{"static (block)", "static (cyclic)", "dynamic"}) {
            System.out.printf("%-15s", type);
        }
        System.out.println();

        // Print the results for each thread count
        for (int threadCount : threadCounts) {
            System.out.printf("%-15d", threadCount);
            for (String type : new String[]{"static (block)", "static (cyclic)", "dynamic"}) {
                Result result = results.get(type).get(threadCount);
                System.out.printf("%-15f", (1.0 / result.totalExecutionTime));
            }
            System.out.println();
        }
    }
}