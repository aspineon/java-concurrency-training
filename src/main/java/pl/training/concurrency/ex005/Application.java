package pl.training.concurrency.ex005;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Application {

    private static final int PRINTER_QUEUE_SIZE = 10;
    private static final int THREADS_COUNT = 10;

    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Condition queueIsFull = lock.newCondition();
        Condition queueIsEmpty = lock.newCondition();

        Queue<String> printingQueue = new LinkedList<>();
        new Thread(new Printer(printingQueue, lock, queueIsFull, queueIsEmpty)).start();
        for (int threadIndex = 0; threadIndex < THREADS_COUNT; threadIndex++) {
            new Thread(new Employee(printingQueue, PRINTER_QUEUE_SIZE, lock, queueIsFull, queueIsEmpty)).start();
        }
    }

}
