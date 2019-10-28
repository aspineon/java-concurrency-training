package pl.training.concurrency.ex003;

import java.util.Optional;
import java.util.Queue;

public class Processor implements Runnable {

    private static final int SLEEP_TIME = 3_000;

    private final Queue<Integer> values;

    public Processor(Queue<Integer> values) {
        this.values = values;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted() && !values.isEmpty()) {
                Thread.sleep(SLEEP_TIME);
                Optional.ofNullable(values.poll()).ifPresent(this::printResult);
            }
        } catch (InterruptedException exception) {
            System.out.println("Preparing to stop...");
        }
        finally {
            System.out.println("Saving remaining values...");
        }
    }

    private void printResult(int value) {
        System.out.printf("Result for value %d: %s\n", value, value % 2 == 0);
    }

}
