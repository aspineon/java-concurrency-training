package pl.training.concurrency.ex014;

import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Exchanger;

public class Consumer implements Runnable {

    private static final int MAX_BUFFER_SIZE = 10;

    private List<String> buffer = new ArrayList<>();
    private final Exchanger<List<String>> exchanger;

    public Consumer(Exchanger<List<String>> exchanger) {
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        while (true) {
            try {
                buffer = exchanger.exchange(buffer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Consumer buffer size: " + buffer.size());
            buffer.forEach(System.out::println);
            buffer.clear();
        }
    }

}
