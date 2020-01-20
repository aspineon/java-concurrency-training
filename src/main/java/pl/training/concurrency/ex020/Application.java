package pl.training.concurrency.ex020;

public class Application {

    public static void main(String[] args) throws InterruptedException {
        Worker worker = new Worker();
        for (int index = 0; index < 100; index++) {
            Thread thread = new Thread(worker);
            thread.start();
        }
        Thread.sleep(5_000);
        System.out.println("Canceling threads");
        worker.shouldFinish = true;
    }

}
