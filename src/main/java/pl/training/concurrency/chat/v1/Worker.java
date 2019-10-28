package pl.training.concurrency.chat.v1;

import java.net.Socket;

public class Worker implements Runnable {

    private final MessageWriter writer;
    private final Socket socket;
    private final Workers workers;

    Worker(Socket socket, Workers workers) {
        this.socket = socket;
        this.workers = workers;
        writer = new MessageWriter(socket);
    }

    @Override
    public void run() {
       new MessageReader(socket, workers::broadcast, () -> workers.remove(this)).read();
    }

    void send(String message) {
        writer.write(message);
    }

}
