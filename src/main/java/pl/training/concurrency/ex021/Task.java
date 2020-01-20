package pl.training.concurrency.ex021;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Task {

    public synchronized void execute() {
        ArrayList<Integer> data = new ArrayList<>();
        Random random = new Random();
        for (int index = 0; index < 1_000_000; index++) {
            data.add(random.nextInt(1_000));
        }
        List<Integer> result = data.stream()
                .map(value -> random.nextInt(10) * 2 + value)
                .collect(Collectors.toList());
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       //System.out.println(result);
    }

}
