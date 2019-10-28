package pl.training.concurrency.chat.v2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

class MessageReader {

    private final Consumer<String> onMessage;
    private final Logger logger = Logger.getLogger(getClass().getName());

    private BufferedReader reader;
    private Runnable onClose;

    MessageReader(InputStream inputStream, Consumer<String> onMessage) {
        this.onMessage = onMessage;
        reader = new BufferedReader(new InputStreamReader(inputStream));
    }

    MessageReader(Socket socket, Consumer<String> onMessage, Runnable onClose) {
        this.onMessage = onMessage;
        this.onClose = onClose;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Creating input stream failed - " + ex.getMessage());
        }
    }

    void read() {
        String message;
        try {
            while ((message = reader.readLine()) != null) {
                onMessage.accept(message);
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Read message failed - " + ex.getMessage());
        }
        finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (onClose != null) {
                onClose.run();
            }
        }
    }

}
