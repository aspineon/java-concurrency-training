package pl.training.concurrency.chat.v1;

import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;

public class ChatClient {

    private final Consumer<String> onMessage;
    private final Runnable readFromSocket;
    private final Runnable readFromConsole;

    private ChatClient(String host, int port, String user) throws IOException {
        Socket socket = new Socket(host, port);
        onMessage = message -> new MessageWriter(socket).write(user + ": " + message);
        readFromSocket = () -> new MessageReader(socket, System.out::println, () -> {}).read();
        readFromConsole = () -> new MessageReader(System.in, onMessage).read();
    }

    private void start() {
        new Thread(readFromSocket).start();
        Thread consoleMessageReader = new Thread(readFromConsole);
        consoleMessageReader.setDaemon(true);
        consoleMessageReader.start();
    }

    public static void main(String[] args) throws IOException {
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        String name = args[2];
        new ChatClient(host, port, name).start();
    }

}
