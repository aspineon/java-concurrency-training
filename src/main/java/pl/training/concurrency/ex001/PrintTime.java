package pl.training.concurrency.ex001;

public class PrintTime implements Runnable {

    @Override
    public void run() {
        while (true) {
            System.out.printf("Current time: %d ns (current thread: %s)\n", System.nanoTime(), Thread.currentThread().getName());
        }
    }

}
