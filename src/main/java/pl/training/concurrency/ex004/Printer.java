package pl.training.concurrency.ex004;

import java.util.Queue;
import java.util.Random;
import java.util.UUID;

public class Printer implements Runnable {

    private static final int MAX_SLEEP_TIME = 200;

    private final Queue<String> printingQueue;
    private final Random random = new Random();

    public Printer(Queue<String> printingQueue) {
        this.printingQueue = printingQueue;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (printingQueue) {
                waitIfQueueIsEmpty();
                printDocument();
            }
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
        printingQueue.notifyAll();
    }

    private void waitIfQueueIsEmpty() {
        while (printingQueue.isEmpty()) {
            try {
                printingQueue.wait();
            } catch (InterruptedException e) {
                System.out.println("Printing was interrupted...");
            }
        }
    }

}
