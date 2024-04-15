import java.util.*;
import java.lang.*;

public class MatrixMultiplicationThread extends Thread {
    private int currentThreadIndex;
    private int rowsInMatrixA;
    private int threadCount;
    private int colsInMatrixB;
    private int commonDimension;
    private int[][] matrixA;
    private int[][] matrixB;
    private int[][] resultMatrix;
    private long executionTime;

    public MatrixMultiplicationThread(int currentThreadIndex, int rowsInMatrixA, int threadCount, int colsInMatrixB,
            int commonDimension, int[][] matrixA, int[][] matrixB, int[][] resultMatrix) {
        this.currentThreadIndex = currentThreadIndex;
        this.rowsInMatrixA = rowsInMatrixA;
        this.threadCount = threadCount;
        this.colsInMatrixB = colsInMatrixB;
        this.commonDimension = commonDimension;
        this.matrixA = matrixA;
        this.matrixB = matrixB;
        this.resultMatrix = resultMatrix;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();

        for (int rowIndex = currentThreadIndex; rowIndex < rowsInMatrixA; rowIndex += threadCount) {
            for (int colIndex = 0; colIndex < colsInMatrixB; colIndex++) {
                for (int elementIndex = 0; elementIndex < commonDimension; elementIndex++) {
                    resultMatrix[rowIndex][colIndex] += matrixA[rowIndex][elementIndex]
                            * matrixB[elementIndex][colIndex];
                }
            }
        }

        long endTime = System.currentTimeMillis();

        executionTime = endTime - startTime;
    }

    public int[][] getResultMatrix() {
        return resultMatrix;
    }

    public long getExecutionTime() {
        return executionTime;
    }
}