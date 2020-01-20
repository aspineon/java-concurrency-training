package pl.training.concurrency.ex022;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;

public class PriorityBlockingQueueExample {

    public static void main(String[] args) {
        PriorityBlockingQueue<Integer> priorityBlockingQueue = new PriorityBlockingQueue<>();
        priorityBlockingQueue.add(3);
        priorityBlockingQueue.add(1);
        priorityBlockingQueue.add(9);
        System.out.println(priorityBlockingQueue.poll());
        System.out.println(priorityBlockingQueue.poll());
        System.out.println(priorityBlockingQueue.poll());

        Thread thread = new Thread(() -> {
            Random random = new Random();
            while (true) {
                priorityBlockingQueue.add(random.nextInt(1_000));
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        while (true) {
            System.out.println(priorityBlockingQueue.poll());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
