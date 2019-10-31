package pl.training.concurrency.ex019;

import java.util.concurrent.Phaser;

public class TaskWithPhases implements Runnable, Comparable<Long> {

    private static final int TIMEOUT = 500;
    private long priority;
    private Phaser phaser;

    public TaskWithPhases(long priority, Phaser phaser) {
        this.priority = priority;
        this.phaser = phaser;
    }

    @Override
    public int compareTo(Long priority) {
        if (this.priority < priority) {
            return 1;
        }
        if (this.priority > priority) {
            return -1;
        }
        return 0;
    }

    @Override
    public void run() {
        phaser.arrive();
        System.out.printf("%s: Starting phase %d\n", Thread.currentThread().getName(), phaser.getPhase());
        sleep();
        phaser.arriveAndAwaitAdvance();
        System.out.printf("%s: Starting phase %d\n", Thread.currentThread().getName(), phaser.getPhase());
        sleep();
        phaser.arriveAndAwaitAdvance();
        System.out.printf("%s: Starting phase %d\n", Thread.currentThread().getName(), phaser.getPhase());
        sleep();
        phaser.arriveAndAwaitAdvance();
        System.out.printf("%s: Starting phase %d\n", Thread.currentThread().getName(), phaser.getPhase());
        sleep();
        phaser.arriveAndDeregister();
    }

    private void sleep() {
        try {
            Thread.sleep(TIMEOUT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
