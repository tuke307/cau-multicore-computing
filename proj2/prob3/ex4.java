package proj2.prob3;

import java.util.concurrent.CyclicBarrier;

public class ex4 {
    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(3, () -> System.out.println("Barrier reached"));
        
        Runnable task = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " waiting at barrier");
                barrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        new Thread(task).start();
        new Thread(task).start();
        new Thread(task).start();
    }
}
