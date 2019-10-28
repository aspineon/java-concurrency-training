package pl.training.concurrency.ex008;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Application {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Callable<Integer> sum = new Sum(2, 4);
        FutureTask<Integer> futureTask = new FutureTask<>(sum);
        new Thread(futureTask).start();

        while (!futureTask.isDone()) {
            System.out.println("Waiting...");
            Thread.sleep(2_000);
        }
        System.out.printf("Result: %d", futureTask.get());
     }

}
