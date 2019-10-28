package pl.training.concurrency.ex004;

import java.util.Random;
import java.util.UUID;

public class PrintDocument {

    private static final int MAX_SLEEP_TIME = 200;

    private final Random random = new Random();

    public void execute() {
        try {
            Thread.sleep(random.nextInt(MAX_SLEEP_TIME));
        } catch (InterruptedException e) {
            System.out.println("Printing was interrupted...");
        }
        System.out.printf("Printing (task id: %s)\n", UUID.randomUUID());
    }

}
