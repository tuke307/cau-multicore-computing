import java.util.*;
import java.lang.*;

// command-line execution example) java MatmultD 6 < mat500.txt
// 6 means the number of threads to use
// < mat500.txt means the file that contains two matrices is given as standard input
public class MatmultD_static {
    private static Scanner sc = new Scanner(System.in);
    
    public static void main(String[] args) throws InterruptedException {
        int a[][] = readMatrix();
        int b[][] = readMatrix();

        int[] threadCounts = {1, 2, 4, 6, 8, 10, 12, 14, 16, 32};
        long[] execTimes = new long[threadCounts.length];

        System.out.println("Thread Count | Execution Time (ms)");
        System.out.println("-------------|-------------------");

        for (int i = 0; i < threadCounts.length; i++) {
            int thread_no = threadCounts[i];
            long startTime = System.currentTimeMillis();
            int[][] c = multMatrix(a, b, thread_no);
            long endTime = System.currentTimeMillis();

            execTimes[i] = endTime - startTime;

            System.out.printf("%13d | %17d\n", thread_no, execTimes[i]);
        }

        System.out.println("\nThread Count | Performance (1/ms)");
        System.out.println("-------------|-------------------");

        for (int i = 0; i < threadCounts.length; i++) {
            int thread_no = threadCounts[i];
            double performance = 1.0 / execTimes[i];

            System.out.printf("%13d | %17.4f\n", thread_no, performance);
        }
    }

    public static int[][] readMatrix() {
        int rows = sc.nextInt();
        int cols = sc.nextInt();
        int[][] result = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = sc.nextInt();
            }
        }
        return result;
    }

    private static void printMatrix(int[][] mat) {
        System.out.println("Matrix[" + mat.length + "][" + mat[0].length + "]");
        int rows = mat.length;
        int columns = mat[0].length;
        int sum = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.printf("%4d ", mat[i][j]);
                sum += mat[i][j];
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("Matrix Sum = " + sum + "\n");
    }

    public static int[][] multMatrix(int a[][], int b[][], int thread_no) throws InterruptedException {
        if (a.length == 0)
            return new int[0][0];
        if (a[0].length != b.length)
            return null; // invalid dims

        int n = a[0].length;
        int m = a.length;
        int p = b[0].length;
        int ans[][] = new int[m][p];

        Thread[] threads = new Thread[thread_no];
        for (int t = 0; t < thread_no; t++) {
            final int threadId = t;
            threads[t] = new Thread(() -> {
                for (int i = threadId; i < m; i += thread_no) {
                    for (int j = 0; j < p; j++) {
                        for (int k = 0; k < n; k++) {
                            ans[i][j] += a[i][k] * b[k][j];
                        }
                    }
                }
            });
            threads[t].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        return ans;
    }
}