package pl.training.concurrency.chat.v3;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Connection {

    private final Logger logger = Logger.getLogger(getClass().getName());

    private PrintWriter writer;

    Connection(Socket socket) {
        try {
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Creating output stream failed - " + ex.getMessage());
        }
    }

    void send(String message) {
        writer.println(message);
    }

}
