package pl.training.concurrency.ex003;

public class BeforeShutdownInfo implements Runnable {

    @Override
    public void run() {
        System.out.println("Performing before shutdown tasks...");
    }

}
