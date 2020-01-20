package pl.training.concurrency.ex022;

import java.util.concurrent.ConcurrentLinkedDeque;

public class ConcurrentLinkDequeExample {

    public static void main(String[] args) {
        ConcurrentLinkedDeque<String> linkedDeque = new ConcurrentLinkedDeque<>();
        for (int index = 0; index < 1_000; index++) {
            new Thread(new AddTask(linkedDeque)).start();
            new Thread(new PoolTask(linkedDeque)).start();
        }
    }

}
