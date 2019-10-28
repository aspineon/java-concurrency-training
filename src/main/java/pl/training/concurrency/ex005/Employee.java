package pl.training.concurrency.ex005;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Employee implements Runnable {

    private static final int MAX_SLEEP_TIME = 300;
    private final Queue<String> printingQueue;
    private final int tasksLimit;
    private final Lock lock;
    private final Condition queueIsFull;
    private final Condition queueIsEmpty;
    private final Random random = new Random();

    public Employee(Queue<String> printingQueue, int tasksLimit, Lock lock, Condition queueIsFull, Condition queueIsEmpty) {
        this.printingQueue = printingQueue;
        this.tasksLimit = tasksLimit;
        this.lock = lock;
        this.queueIsFull = queueIsFull;
        this.queueIsEmpty = queueIsEmpty;
    }

    @Override
    public void run() {
        while (true) {
            lock.lock();
            waitIfQueueIsFull();
            addDocument();
            lock.unlock();
        }
    }

    private void addDocument() {
        try {
            Thread.sleep(random.nextInt(MAX_SLEEP_TIME));
        } catch (InterruptedException e) {
            System.out.println("Employee was interrupted...");
        }
        System.out.println("Creating new task...");
        printingQueue.add("Document");
        queueIsEmpty.signal();
    }

    private void waitIfQueueIsFull() {
        while (tasksLimit == printingQueue.size()) {
            try {
                System.out.println("Queue limit reached...");
                queueIsFull.await();
            } catch (InterruptedException e) {
                System.out.println("Employee was interrupted...");
            }
        }
    }

}
