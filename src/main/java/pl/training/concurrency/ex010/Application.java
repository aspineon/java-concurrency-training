package pl.training.concurrency.ex010;

import java.util.UUID;
import java.util.concurrent.Semaphore;

public class Application {

    private static final int THREADS_COUNT = 10;

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(5);
        PrintingQueue printingQueue = new PrintingQueue(semaphore);
        for (int threadIndex = 0; threadIndex < THREADS_COUNT; threadIndex++) {
            new Thread(() -> printingQueue.print(UUID.randomUUID().toString())).start();
        }
    }

}
