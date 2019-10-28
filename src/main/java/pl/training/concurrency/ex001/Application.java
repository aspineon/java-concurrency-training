package pl.training.concurrency.ex001;

public class Application {

    public static void main(String[] args) {
        PrintTime printTime = new PrintTime();
        new Thread(printTime).start();
        new Thread(printTime).start();
        new PrintTimeThread().start();
    }

}
