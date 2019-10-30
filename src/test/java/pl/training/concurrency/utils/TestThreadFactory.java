package pl.training.concurrency.utils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public class TestThreadFactory implements ThreadFactory {

    private AtomicLong counter = new AtomicLong();

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable, "T" + counter.incrementAndGet());
        thread.setPriority(6);
        return thread;
    }

}
