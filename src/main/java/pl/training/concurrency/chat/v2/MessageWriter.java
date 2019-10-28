package pl.training.concurrency.chat.v2;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

class MessageWriter {

    private final Logger logger = Logger.getLogger(getClass().getName());

    private PrintWriter writer;

    MessageWriter(Socket socket) {
        try {
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Creating output stream failed - " + ex.getMessage());
        }
    }

    void write(String message) {
        writer.println(message);
    }

}
