package Helper;

import java.util.*;
import java.lang.*;

public class Functions {
    private static Scanner sc = new Scanner(System.in);

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

    public static int calculateMatrix(int[][] mat, boolean print) {
        if (print)
            System.out.println("Matrix[" + mat.length + "][" + mat[0].length + "]");

        int rows = mat.length;
        int columns = mat[0].length;
        int sum = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (print)
                    System.out.printf("%4d ", mat[i][j]);
                sum += mat[i][j];
            }
            if (print)
                System.out.println();
        }
        if (print) {
            System.out.println();
            System.out.println("Matrix Sum = " + sum + "\n");
        }

        return sum;

    }
}
