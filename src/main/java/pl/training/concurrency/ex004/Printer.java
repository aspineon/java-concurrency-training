package pl.training.concurrency.ex004;

import java.util.Optional;
import java.util.Queue;

public class Printer implements Runnable {

    private final Queue<PrintDocument> printingQueue;

    public Printer(Queue<PrintDocument> printingQueue) {
        this.printingQueue = printingQueue;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (printingQueue) {
                waitIfQueueIsEmpty();
                print();
            }
        }
    }

    private void print() {
        Optional.ofNullable(printingQueue.poll()).ifPresent(PrintDocument::execute);
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
