package pl.training.concurrency.ex017;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

public class Application {

    private static final int PRODUCTS_COUNT = 100_000_000;
    private static final int CHUNK_SIZE = 50_000_000;
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getNumberInstance();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        List<Product> products = new ArrayList<>();
        for (int index = 0; index < PRODUCTS_COUNT; index++) {
            products.add(new Product(1));
        }

        for (int index = 0; index < 10; index++) {
            ForkJoinPool forkJoinPool = new ForkJoinPool();
            ChangePriceTask changePriceTask = new ChangePriceTask(products, 1, 0, products.size() - 1, CHUNK_SIZE);
            System.out.println("Start...");
            long startTime = System.nanoTime();
            forkJoinPool.execute(changePriceTask);
            changePriceTask.get();
            System.out.println("Time: " + NUMBER_FORMAT.format(System.nanoTime() - startTime) + " ns");
        }

    }

}
