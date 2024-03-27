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
    
    public static int[][] multMatrix(int[][] matrixA, int[][] matrixB, int threadCount) throws InterruptedException {
        // Check if matrixA is empty
        if (matrixA.length == 0) {
            return new int[0][0]; // Return an empty matrix
        }

        // Check if the number of columns in matrixA is not equal to the number of rows in matrixB
        if (matrixA[0].length != matrixB.length) {
            return null; // Return null for invalid dimensions
        }

        // Get the common dimension between the two matrices
        int commonDimension = matrixA[0].length;

        // Get the number of rows in matrixA
        int rowsInMatrixA = matrixA.length;

        // Get the number of columns in matrixB
        int colsInMatrixB = matrixB[0].length;

        // Create a new matrix to store the result of the multiplication
        int[][] resultMatrix = new int[rowsInMatrixA][colsInMatrixB];

        // Create an array of threads
        Thread[] threads = new Thread[threadCount];

        // Loop over each thread
        for (int threadIndex = 0; threadIndex < threadCount; threadIndex++) {
            // Capture the thread index for use in the thread
            final int currentThreadIndex = threadIndex;

            // Create a new thread
            threads[threadIndex] = new Thread(() -> {
                // Each thread processes a subset of the rows of matrixA
                for (int rowIndex = currentThreadIndex; rowIndex < rowsInMatrixA; rowIndex += threadCount) {
                    // Multiply the current row of matrixA by each column of matrixB
                    for (int colIndex = 0; colIndex < colsInMatrixB; colIndex++) {
                        // Multiply and sum the corresponding elements in the current row and column
                        for (int elementIndex = 0; elementIndex < commonDimension; elementIndex++) {
                            resultMatrix[rowIndex][colIndex] += matrixA[rowIndex][elementIndex] * matrixB[elementIndex][colIndex];
                        }
                    }
                }
            });

            // Start the thread
            threads[threadIndex].start();
        }

        // Wait for all threads to finish
        for (Thread thread : threads) {
            thread.join();
        }

        // Return the result matrix
        return resultMatrix;
    }
}