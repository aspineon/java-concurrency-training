package pl.training.concurrency.chat.v3;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import java.io.IOException;
import java.net.Socket;

public class ChatClient {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private void start(String host, int port, String user) throws IOException {
        Runtime.getRuntime().addShutdownHook(new Thread(compositeDisposable::dispose));
        Socket socket = new Socket(host, port);
        Connection connection = new Connection(socket);

        compositeDisposable.add(ObservableReader.from(System.in)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .map(message -> user + ": " + message)
                .subscribe(connection::send));

        compositeDisposable.add(ObservableReader.from(socket).subscribe(System.out::println));
    }

    public static void main(String[] args) throws IOException {
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        String name = args[2];
        new ChatClient().start(host, port, name);
    }

}
