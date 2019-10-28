package pl.training.concurrency.ex004;

import java.util.LinkedList;
import java.util.Queue;

public class Application {

    private static final int PRINTER_QUEUE_SIZE = 10;
    private static final int THREADS_COUNT = 10;

    public static void main(String[] args) {
        Queue<PrintDocument> printingQueue = new LinkedList<>();
        new Thread(new Printer(printingQueue)).start();
        for (int threadIndex = 0; threadIndex < THREADS_COUNT; threadIndex++) {
            new Thread(new Employee(printingQueue, PRINTER_QUEUE_SIZE)).start();
        }
    }

}
