package pl.training.concurrency.chat.v3;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

import java.net.ServerSocket;
import java.net.Socket;

public class ObservableServerSocket implements ObservableOnSubscribe<Socket> {

    private final ServerSocket serverSocket;

    ObservableServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void subscribe(ObservableEmitter<Socket> emitter) throws Exception {
        while (true) {
            Socket socket = serverSocket.accept();
            emitter.onNext(socket);
        }
    }

}
