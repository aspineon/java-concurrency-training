package pl.training.concurrency.chat.v1;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Worker implements Runnable {

    private static final String END_SESSION_COMMAND = "\\q";

    private final Logger logger = Logger.getLogger(getClass().getName());
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
        new MessageReader(socket, this::onMessage, () -> workers.remove(this)).read();
    }

    private void onMessage(String message) {
        if (message.endsWith(END_SESSION_COMMAND)) {
            closeSocket();
        } else {
            workers.broadcast(message);
        }
    }

    private void closeSocket() {
        try {
            socket.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Closing socked failed - " + ex.getMessage());
        }
    }

    void send(String message) {
        writer.write(message);
    }

}
