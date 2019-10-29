package pl.training.concurrency.ex011;

import pl.training.concurrency.ex010.PrintingQueue;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class Application {

    private static final int THREADS_COUNT = 10;

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(5);
        Meeting meeting = new Meeting(countDownLatch);
        new Thread(meeting).start();
        meeting.addParticipant("Jan");
        Thread.sleep(2_000);
        meeting.addParticipant("Marcin");
        meeting.addParticipant("Marek");
        meeting.addParticipant("Anna");
        Thread.sleep(1_000);
        meeting.addParticipant("Adam");
    }

}
