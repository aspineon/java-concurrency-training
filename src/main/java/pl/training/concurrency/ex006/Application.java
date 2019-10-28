package pl.training.concurrency.ex006;

import java.util.Random;
import java.util.UUID;

public class Application {

    private static final int THREADS_COUNT = 10;
    private static final KeyValueStore<Integer, String> keyValueStore = new KeyValueStore<>();

    public static void main(String[] args) {

        for (int threadIndex = 0; threadIndex < THREADS_COUNT; threadIndex++) {
            new Thread(Application::doSomething).start();
        }
    }

    private static void doSomething() {
        Random random = new Random();
        while (true) {
            if (random.nextBoolean()) {
                keyValueStore.put(random.nextInt(100), UUID.randomUUID().toString());
            } else {
                System.out.println(keyValueStore.get(random.nextInt(100)));
            }
        }
    }

}
