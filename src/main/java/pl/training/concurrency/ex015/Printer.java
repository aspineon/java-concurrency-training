package pl.training.concurrency.ex015;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;

public class Printer implements Runnable {

    private CompletionService<String> completionService;

    public Printer(CompletionService<String> completionService) {
        this.completionService = completionService;
    }

    @Override
    public void run() {
        try {
            System.out.println("Report: " + completionService.take().get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

}
