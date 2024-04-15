import java.util.*;
import java.lang.*;
import Helper.Functions;
import Helper.Result;

// command-line execution example) java MatmultD_static_block.java 6 < mat500.txt
// 6 means the number of threads to use
// < mat500.txt means the file that contains two matrices is given as standard input
public class MatmultD_static_block {
    private static int NUM_THREAD = 1;

    public static void main(String[] args) throws InterruptedException {
        if (args.length == 1) {
            NUM_THREAD = Integer.parseInt(args[0]);
        }

        int a[][] = Functions.readMatrix();
        int b[][] = Functions.readMatrix();

        System.out.println("--static block thread handling--");
        System.out.println("using " + NUM_THREAD + " threads");

        Result result = multMatrix(a, b, NUM_THREAD);

        System.out.println("Program Execution Time: " + result.totalExecutionTime + "ms");
        for (int i = 0; i < result.threadExecutionTimes.length; i++) {
            System.out.println("Execution time of thread " + (i + 1) + ": " + result.threadExecutionTimes[i] + "ms");
        }
    }

    public static Result multMatrix(int[][] matrixA, int[][] matrixB, int threadCount) {
        if (matrixA.length == 0) {
            return new Result(0, new long[0], new int[0][0]);
        }

        // Check if the number of columns in matrixA is not equal to the number of rows
        // in matrixB
        if (matrixA[0].length != matrixB.length) {
            return null; // Return null for invalid dimensions
        }

        int commonDimension = matrixA[0].length;
        int rowsInMatrixA = matrixA.length;
        int colsInMatrixB = matrixB[0].length;
        int[][] resultMatrix = new int[rowsInMatrixA][colsInMatrixB];
        long startTime = System.currentTimeMillis();
        long[] threadExecutionTimes = new long[threadCount];

        MatrixMultiplicationThread[] threads = new MatrixMultiplicationThread[threadCount];

        // Loop over each thread
        for (int threadIndex = 0; threadIndex < threadCount; threadIndex++) {
            final int currentThreadIndex = threadIndex;

            threads[threadIndex] = new MatrixMultiplicationThread(currentThreadIndex, rowsInMatrixA, threadCount,
                    colsInMatrixB, commonDimension, matrixA, matrixB, resultMatrix);
            threads[threadIndex].start();
        }

        // Wait for all threads to finish
        for (MatrixMultiplicationThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();
        long totalExecutionTime = endTime - startTime;
        for (int i = 0; i < threadCount; i++) {
            threadExecutionTimes[i] = threads[i].getExecutionTime();
        }

        return new Result(totalExecutionTime, threadExecutionTimes, resultMatrix);
    }
}