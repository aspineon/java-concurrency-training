package pl.training.concurrency.ex019;

import java.util.concurrent.locks.Lock;

public class TaskWithLock implements Runnable {

    private static final int TIMEOUT = 500;

    private Lock lock;

    public TaskWithLock(Lock lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            lock.lock();
            System.out.println("After lock: " + Thread.currentThread().getName());
            try {
                Thread.sleep(TIMEOUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.unlock();
        }
    }

}
