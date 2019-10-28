package pl.training.concurrency.chat.v2;

import java.io.IOException;
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
       new MessageReader(socket, message -> {
           if (message.endsWith("\\q")) {
               try {
                   socket.close();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           } else {
               workers.broadcast(message);
           }
       }, () -> workers.remove(this)).read();
    }

    void send(String message) {
        writer.write(message);
    }

}
