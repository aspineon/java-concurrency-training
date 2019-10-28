package pl.training.concurrency.chat.v2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Workers {

    private final List<Worker> workers = new ArrayList<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    void add(Worker worker) {
        lock.writeLock().lock();
        workers.add(worker);
        lock.writeLock().unlock();
    }

    void broadcast(String message) {
        lock.readLock().lock();
        workers.forEach(worker -> worker.send(message));
        lock.readLock().unlock();
    }

    void remove(Worker worker) {
        lock.writeLock().lock();
        workers.remove(worker);
        lock.writeLock().unlock();
    }

}
