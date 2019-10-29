package pl.training.concurrency.ex013;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Phaser;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class Search implements Runnable {

    private final Path path;
    private final String query;
    private final Predicate<File> predicate;
    private final Phaser phaser;
    private Set<File> files = new HashSet<>();

    public Search(Path path, String query, Predicate<File> predicate, Phaser phaser) {
        this.path = path;
        this.query = query;
        this.predicate = predicate;
        this.phaser = phaser;
    }

    @Override
    public void run() {
        nextPhase();
        process(path.toFile());
        if (files.isEmpty()) {
            endPhase();
            return;
        }
        System.out.printf("%s: Finished with %d results\n", Thread.currentThread().getName(), files.size());
        nextPhase();
        files = files.stream().filter(predicate).collect(toSet());
        if (files.isEmpty()) {
            endPhase();
            System.out.printf("%s: Finished with %d results\n", Thread.currentThread().getName(), files.size());
            return;
        }
        System.out.printf("%s: Finished with %d results\n", Thread.currentThread().getName(), files.size());
        nextPhase();
        files.forEach(file -> System.out.println(file.getAbsolutePath()));
    }

    private void nextPhase() {
        phaser.arriveAndAwaitAdvance();
        System.out.printf("%s: Starting phase %d\n", Thread.currentThread().getName(), phaser.getPhase());
    }

    private void endPhase() {
        System.out.printf("%s: Finished with no results\n", Thread.currentThread().getName());
        phaser.arriveAndDeregister();
    }

    private void process(File file) {
        if (file.isDirectory()) {
            processDirectory(file);
        } else {
            processFile(file);
        }
    }

    private void processDirectory(File file) {
        File[] files = file.listFiles();
        if (files != null) {
            Arrays.stream(files).forEach(this::process);
        }
    }

    private void processFile(File file) {
        if (file.getName().contains(query)) {
            files.add(file);
        }
    }

}
