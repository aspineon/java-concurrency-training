package pl.training.concurrency.ex018;

import java.util.concurrent.Callable;

public class Multiply implements Callable<Integer> {

    private static final int SLEEP_TIME = 3_000;

    private final int firstValue;
    private final int secondValue;

    public Multiply(int firstValue, int secondValue) {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
    }

    @Override
    public Integer call() {
        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
        System.out.println("Done...");
        return firstValue * secondValue;
    }

}
