package pl.training.concurrency.ex010;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrintingQueue {

    private static final int MAX_TIMEOUT = 10_000;

    private final Logger logger = Logger.getLogger(getClass().getName());
    private final Semaphore semaphore;
    private final Random random = new Random();

    public PrintingQueue(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    public void print(String document) {
        try {
            semaphore.acquire();
            logger.log(Level.INFO, "Thread " + Thread.currentThread().getName() + " acquired lock");
            int printTime = random.nextInt(MAX_TIMEOUT);
            Thread.sleep(printTime);
            System.out.println(document);
            semaphore.release();
            logger.log(Level.INFO, "Thread " + Thread.currentThread().getName() + " released lock");
        } catch (InterruptedException ex) {
            logger.log(Level.SEVERE, "Thread interrupted " + ex.getMessage());
        }
    }

}
