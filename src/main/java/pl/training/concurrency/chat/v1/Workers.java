package pl.training.concurrency.chat.v1;

import java.util.ArrayList;
import java.util.List;

class Workers {

    private final List<Worker> workers = new ArrayList<>();

    synchronized void add(Worker worker) {
        workers.add(worker);
    }

    synchronized void broadcast(String message) {
        workers.forEach(worker -> worker.send(message));
    }

    synchronized void remove(Worker worker) {
        workers.remove(worker);
    }

}
