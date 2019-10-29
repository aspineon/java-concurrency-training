package pl.training.concurrency.ex011;

import java.util.concurrent.CountDownLatch;

public class Meeting implements Runnable {

    private final CountDownLatch countDownLatch;

    public Meeting(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    public void addParticipant(String name) {
        countDownLatch.countDown();
        System.out.printf("%s has joined conference\n", name);
    }

    @Override
    public void run() {
        System.out.println("Waiting for participants...");
        try {
            countDownLatch.await();
            System.out.println("Meeting has started...");
        } catch (InterruptedException ex) {
            System.out.println("Meeting is over...");
        }

    }

}
