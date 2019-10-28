package pl.training.concurrency.ex003;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Application {

    public static void main(String[] args) throws InterruptedException {
        Runtime.getRuntime().addShutdownHook(new Thread(new BeforeShutdownInfo()));
        Queue<Integer> values = new LinkedList<>(List.of(1, 2, 3, 4, 5, 6));
        Processor processor = new Processor(values);
        Thread thread = new Thread(processor);
        thread.start();
        thread.join(10_000);
        thread.interrupt();
    }

}
