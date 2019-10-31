package pl.training.concurrency.ex019;

import edu.umd.cs.mtc.TestFramework;

public class TCRunner {

    public static void main(String[] args) throws Throwable {
        ProducerConsumerTest producerConsumerTest = new ProducerConsumerTest();
        TestFramework.runManyTimes(producerConsumerTest, 1);
    }

}
