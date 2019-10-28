package pl.training.concurrency.ex005;

import java.util.Queue;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Printer implements Runnable {

    private static final int MAX_SLEEP_TIME = 200;

    private final Queue<String> printingQueue;
    private final Lock lock;
    private final Condition queueIsFull;
    private final Condition queueIsEmpty;
    private final Random random = new Random();

    public Printer(Queue<String> printingQueue, Lock lock, Condition queueIsFull, Condition queueIsEmpty) {
        this.printingQueue = printingQueue;
        this.lock = lock;
        this.queueIsFull = queueIsFull;
        this.queueIsEmpty = queueIsEmpty;
    }

    @Override
    public void run() {
        while (true) {
            lock.lock();
            waitIfQueueIsEmpty();
            printDocument();
            lock.unlock();
        }
    }

    private void printDocument() {
        try {
            Thread.sleep(random.nextInt(MAX_SLEEP_TIME));
        } catch (InterruptedException e) {
            System.out.println("Printing was interrupted...");
        }
        System.out.printf("Printing (task id: %s)\n", UUID.randomUUID());
        printingQueue.poll();
        queueIsFull.signal();
    }

    private void waitIfQueueIsEmpty() {
        while (printingQueue.isEmpty()) {
            try {
                queueIsEmpty.await();
            } catch (InterruptedException e) {
                System.out.println("Printing was interrupted...");
            }
        }
    }

}
