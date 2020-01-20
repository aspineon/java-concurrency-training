package pl.training.concurrency.ex022;

import java.util.Queue;

public class AddTask implements Runnable {

    private final Queue<String> list;

    public AddTask(Queue<String> list) {
        this.list = list;
    }

    @Override
    public void run() {
        for (int index = 0; index < 5_500; index++) {
            list.add("Element: " + index);
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
