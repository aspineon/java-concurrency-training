package pl.training.concurrency.ex021;

public class Worker implements Runnable {

    private Task task;

    public Worker(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        Thread thread = Thread.currentThread();
        if (thread.getName().equals("Low")) {
          //  thread.setPriority(10);
        }
        long startTime = System.nanoTime();
        System.out.println("Starting + Thread: " + Thread.currentThread().getName());
        task.execute();
        long endTime = (System.nanoTime() - startTime) / 1_000;
        if (thread.getName().equals("Low")) {
          //  thread.setPriority(1);
        }
        System.out.println("Time: " + endTime + ", Thread: " + Thread.currentThread().getName());
    }

}
