package pl.training.concurrency.ex004;

import java.util.Queue;
import java.util.Random;

public class Employee implements Runnable {

    private static final int MAX_SLEEP_TIME = 300;
    private final Queue<PrintDocument> printingQueue;
    private final int tasksLimit;
    private final Random random = new Random();

    public Employee(Queue<PrintDocument> printingQueue, int tasksLimit) {
        this.printingQueue = printingQueue;
        this.tasksLimit = tasksLimit;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (printingQueue) {
                waitIfQueueIsFull();
                createTask();
            }
        }
    }

    private void createTask() {
        try {
            Thread.sleep(random.nextInt(MAX_SLEEP_TIME));
        } catch (InterruptedException e) {
            System.out.println("Employee was interrupted...");
        }
        System.out.println("Creating new task...");
        printingQueue.add(new PrintDocument());
        printingQueue.notify();
    }

    private void waitIfQueueIsFull() {
        while (tasksLimit == printingQueue.size()) {
            try {
                System.out.println("Queue limit reached...");
                printingQueue.wait();
            } catch (InterruptedException e) {
                System.out.println("Employee was interrupted...");
            }
        }
    }

}
