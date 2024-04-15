
package Helper;

import java.util.*;
import java.lang.*;

public class Result {
    public long totalExecutionTime;
    public long[] threadExecutionTimes;
    public int[][] a;

    public Result(long totalExecutionTime, long[] threadExecutionTimes, int[][] a) {
        this.totalExecutionTime = totalExecutionTime;
        this.threadExecutionTimes = threadExecutionTimes;
        this.a = a;
    }
}