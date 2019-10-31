package pl.training.concurrency.ex019;

import org.junit.Before;
import org.junit.Test;
import pl.training.concurrency.utils.TestExecutor;
import pl.training.concurrency.utils.TestPhaser;
import pl.training.concurrency.utils.TestThreadFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskWithPhasesTest {

    private static final int TIMEOUT = 1_000;
    private static final int PARTIES = 3;

    private final Phaser phaser = new TestPhaser(PARTIES);
    private final ThreadPoolExecutor executorService = new TestExecutor(3, 10,
            10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));

    @Before
    public void init() {
        executorService.setThreadFactory(new TestThreadFactory());
        for (int i = 0; i < PARTIES; i++) {
            executorService.execute(new TaskWithPhases(i, phaser));
        }
    }

    @Test
    public void monitorThreads() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(TIMEOUT);
        executorService.shutdown();
    }

}
