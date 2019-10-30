package pl.training.concurrency.ex009;

import java.util.List;
import java.util.concurrent.*;

public class Application {

    private static final int WAIT_TIME = 2;
    private static final int THREADS_COUNT = 2;

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Callable<Integer> sum = new Sum(2, 5);
        ExecutorService executorService = Executors.newFixedThreadPool(THREADS_COUNT);
        Future<Integer> futureTask = executorService.submit(sum);
        executorService.shutdown();
        System.out.println("Waiting...");
        // executorService.awaitTermination(WAIT_TIME, TimeUnit.SECONDS);
        System.out.printf("Result: %d\n", futureTask.get());

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(THREADS_COUNT);
        scheduledExecutorService.schedule(new Multiply(2, 5), WAIT_TIME, TimeUnit.SECONDS);
        scheduledExecutorService.shutdown();

        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(THREADS_COUNT);
        List<Future<Integer>> powers = threadPoolExecutor.invokeAll(List.of(new Multiply(2, 5), new Multiply(2, 6)));
        //threadPoolExecutor.shutdown();
        threadPoolExecutor.awaitTermination(10, TimeUnit.SECONDS);
        powers.stream().map(Future::isDone).forEach(System.out::println);
    }

}
