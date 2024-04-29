package proj2.prob3;

import java.util.concurrent.atomic.AtomicInteger;

public class ex3 {
    public static void main(String[] args) {
        AtomicInteger value = new AtomicInteger(0);
        
        Thread updater = new Thread(() -> {
            System.out.println(value.getAndIncrement());
        });

        updater.start();
    }
}

