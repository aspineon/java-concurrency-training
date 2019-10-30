package pl.training.concurrency.search;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import pl.training.concurrency.search.github.GithubService;
import pl.training.concurrency.search.github.Repository;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Application {

    private GithubService githubService;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private Retrofit createRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private Observable<List<Repository>> getRepositories(String query) {
        return githubService.getRepositories(query);
    }

    private void start() {
        Runtime.getRuntime().addShutdownHook(new Thread(compositeDisposable::dispose));
        githubService = new GithubService(createRetrofit());

        Observable<String> input = ObservableReader.from(System.in);

        //Disposable disposable = input.map(this::getRepositories)
        //        .subscribe(result -> compositeDisposable.add(result.subscribe(System.out::println)));

        Disposable disposable = input.flatMap(this::getRepositories)
                .subscribe(System.out::println);

        compositeDisposable.add(disposable);
    }

    public static void main(String[] args) {
        new Application().start();
    }

    //https://en.wikipedia.org/w/api.php?action=query&format=json&list=search&srsearch=
}
