package pl.training.concurrency.chat.v2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatServer {

    private static final int CONNECTION_LIMIT= 1024;

    private final Workers workers = new Workers();
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final ExecutorService executorService;

    private ChatServer(ExecutorService executorService) {
        this.executorService = executorService;
    }

    private void start(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            listen(serverSocket, port);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Server failed to start - " + ex.getMessage());
        }
    }

    private void listen(ServerSocket serverSocket, int port) throws IOException {
        logger.log(Level.INFO, "Server is listening on port " + port);
        while (true) {
            Socket socket = serverSocket.accept();
            logger.log(Level.INFO, "New connection established...");
            Worker worker = new Worker(socket, workers);
            workers.add(worker);
            executorService.execute(worker);
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(CONNECTION_LIMIT);
        int port = Integer.parseInt(args[0]);
        new ChatServer(executorService).start(port);
    }

}
