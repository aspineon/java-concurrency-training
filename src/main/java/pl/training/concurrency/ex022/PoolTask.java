package pl.training.concurrency.ex022;

import java.util.Deque;

public class PoolTask implements Runnable {

    private final Deque<String> list;

    public PoolTask(Deque<String> list) {
        this.list = list;
    }

    @Override
    public void run() {
        for (int index = 0; index < 10; index++) {
            System.out.println(list.pollFirst());
            //System.out.println(list.pollLast());
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
