package pl.training.concurrency.ex021;

public class Application {

    public static void main(String[] args) throws InterruptedException {
        Task task = new Task();
        Worker worker = new Worker(task);
        Thread threadWithHighPriority = new Thread(worker);
        threadWithHighPriority.setPriority(10);
        threadWithHighPriority.setName("High");
        Thread threadWithLowPriority = new Thread(worker);
        threadWithLowPriority.setPriority(1);
        threadWithLowPriority.setName("Low");
        for (int index = 0; index < 1_000; index++) {
            Thread thread = new Thread(new OtherTask());
            thread.start();
        }
        System.out.println("All medium threads started ");
        threadWithLowPriority.start();
        Thread.sleep(100);
        threadWithHighPriority.start();
    }

}
