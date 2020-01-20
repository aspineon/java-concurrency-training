package pl.training.concurrency.ex022;

import java.util.Date;
import java.util.concurrent.DelayQueue;

public class DelayQueueExample {

    public static void main(String[] args) throws InterruptedException {
        DelayQueue<Event> delayQueue = new DelayQueue<>();
        Date date = new Date();
        date.setTime(date.getTime() + 60 * 1000);
        Event event = new Event(date);
        delayQueue.add(event);
        System.out.println(delayQueue.poll());
        Thread.sleep(60_000);
        System.out.println(delayQueue.poll());
    }

}
