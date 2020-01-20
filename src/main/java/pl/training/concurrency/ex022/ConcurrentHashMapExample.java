package pl.training.concurrency.ex022;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapExample {

    public static void main(String[] args) {
        //HashMap<Integer, Integer> storage = new HashMap<>();
        ConcurrentHashMap<Integer, Integer> storage = new ConcurrentHashMap<>();
        Thread thread = new Thread(() -> {
            Random random = new Random();
            while (true) {
                storage.put(random.nextInt(1_000), random.nextInt());
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        while (true) {
            for (Map.Entry<Integer, Integer> entry : storage.entrySet()) {
                System.out.println(entry.getValue());
            }
        }
    }

}
