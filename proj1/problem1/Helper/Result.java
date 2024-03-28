package Helper;
public class Result {
        public long totalExecutionTime;
        public long[] threadExecutionTimes;
        public int primeCounter;

        public Result(long totalExecutionTime, long[] threadExecutionTimes, int primeCounter) {
            this.totalExecutionTime = totalExecutionTime;
            this.threadExecutionTimes = threadExecutionTimes;
            this.primeCounter = primeCounter;
        }
    }