
package Helper;

import java.util.*;
import java.lang.*;

public class Result {
    public long totalExecutionTime;
    public long[] threadExecutionTimes;
    public int[][] resultMatrix;

    public Result(long totalExecutionTime, long[] threadExecutionTimes, int[][] resultMatrix) {
        this.totalExecutionTime = totalExecutionTime;
        this.threadExecutionTimes = threadExecutionTimes;
        this.resultMatrix = resultMatrix;
    }
}