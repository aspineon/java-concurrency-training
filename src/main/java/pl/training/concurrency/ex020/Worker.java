package pl.training.concurrency.ex020;

public class Worker implements Runnable {

    volatile boolean shouldFinish; // powoduje synchronizacje wartości między wątkami

    @Override
    public void run() {
        while (!shouldFinish) {
            System.out.println(Thread.currentThread().getName());
        }
    }

}
