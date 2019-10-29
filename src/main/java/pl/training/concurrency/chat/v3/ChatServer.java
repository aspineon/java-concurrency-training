package pl.training.concurrency.chat.v3;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatServer {

    private final Logger logger = Logger.getLogger(getClass().getName());
    private final Connections connections = new Connections();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private void start(int port) {
        Runtime.getRuntime().addShutdownHook(new Thread(compositeDisposable::dispose));
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            ObservableServerSocket observableServerSocket = new ObservableServerSocket(serverSocket);
            logger.log(Level.INFO, "Server is listening on port " + port);
            compositeDisposable.add(Observable.create(observableServerSocket).subscribe(this::onNextSocket));
        }  catch (IOException ex) {
            logger.log(Level.SEVERE, "Server failed to start - " + ex.getMessage());
        }
    }

    private void onNextSocket(Socket socket) throws IOException {
        connections.add(new Connection(socket));
        ObservableSocket observableSocket = new ObservableSocket(socket);
        compositeDisposable.add(Observable.create(observableSocket)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())
                .subscribe(connections::broadcast));
    }

    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        new ChatServer().start(port);
    }

}
