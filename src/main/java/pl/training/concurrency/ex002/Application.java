package pl.training.concurrency.ex002;

public class Application {

    public static void main(String[] args) throws InterruptedException {
        Sum sum = new Sum(1, 5);
        Thread thread = new Thread(sum);
        thread.start();
        thread.join();
        System.out.println("Main thread after join...");
    }

}
