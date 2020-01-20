package pl.training.concurrency.ex021;

import java.util.ArrayList;
import java.util.Random;

public class OtherTask implements Runnable{

    @Override
    public void run() {
        ArrayList<Integer> data = new ArrayList<>();
        Random random = new Random();
        for (int index = 0; index < 200_000; index++) {
            data.add(random.nextInt(1_000));
        }
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //System.out.printf("Result: " + data.get(0));
    }

}
