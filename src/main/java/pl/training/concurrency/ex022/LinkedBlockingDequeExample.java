package pl.training.concurrency.ex022;

import java.util.concurrent.LinkedBlockingDeque;

public class LinkedBlockingDequeExample {

    public static void main(String[] args) {
        LinkedBlockingDeque<String> linkedBlockingDeque = new LinkedBlockingDeque<>(3);
        for (int index = 0; index < 1; index++) {
            new Thread(new AddTask(linkedBlockingDeque)).start();
            new Thread(new PoolTask(linkedBlockingDeque)).start();
        }
    }

}
