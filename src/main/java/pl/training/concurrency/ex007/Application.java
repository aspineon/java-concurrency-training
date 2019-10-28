package pl.training.concurrency.ex007;

public class Application {

    public static void main(String[] args) {
        Counter counter = new Counter();
        new Thread(counter).start();
        new Thread(counter).start();
    }

}
