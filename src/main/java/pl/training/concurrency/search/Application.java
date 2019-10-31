package pl.training.concurrency.search;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pl.training.concurrency.search.github.GithubService;
import pl.training.concurrency.search.github.Repository;
import pl.training.concurrency.search.wikipedia.Article;
import pl.training.concurrency.search.wikipedia.WikipediaService;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.ArrayList;
import java.util.List;

public class Application {

    private GithubService githubService = new GithubService(retrofitBuilder("https://api.github.com/"));
    private WikipediaService wikipediaService = new WikipediaService(retrofitBuilder("https://en.wikipedia.org/w/"));
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private Retrofit retrofitBuilder(String url) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private <T> void showThreadInfo(T input) {
        System.out.println("processing item on thread " + Thread.currentThread().getName());
    }

    private List<String> combine(List<String> result,String value) {
        result.add(value);
        return result;
    }

    private boolean hasWhiteSpaces(String text) {
        return text.matches(".*\\s+.*");
    }

    private void sendQuery(String query) {
        Observable<List<Repository>> repositories = githubService.getRepositories(query)
                .subscribeOn(Schedulers.io());
        Observable<List<Article>> articles = wikipediaService.getArticles(query)
                .subscribeOn(Schedulers.io());

        Observable<String> repositoriesNames = repositories.flatMap(Observable::fromIterable)
                .map(Repository::getName)
                .observeOn(Schedulers.newThread());

        Observable<String> articlesNames = articles.flatMap(Observable::fromIterable)
                .map(Article::getTitle)
                .observeOn(Schedulers.newThread());

        Observable<List<String>> namePairs=  Observable.zip(repositoriesNames, articlesNames, List::of)
                .flatMap(Observable::fromIterable)
                .map(String::toLowerCase)
                .filter(this::hasWhiteSpaces)
                .distinct()
                .reduce(new ArrayList<>(), this::combine)
                .toObservable();

        compositeDisposable.add(namePairs.subscribe(System.out::println, System.out::println, () -> System.out.println("Completed")));

    }

    private void start() {
        Runtime.getRuntime().addShutdownHook(new Thread(compositeDisposable::dispose));
        compositeDisposable.add(ObservableReader.from(System.in).subscribe(this::sendQuery));
    }

    public static void main(String[] args) {
        new Application().start();
    }

}

/*

https://proandroiddev.com/understanding-rxjava-subscribeon-and-observeon-744b0c6a41ea

We can specify a thread to execute any operator by using subscribeOn and/or observeOn.

subscribeOn affects upstream operators (operators above the subscribeOn)

observeOn affects downstream operators (operators below the observeOn)

If only subscribeOn is specified, all operators will be be executed on that thread

If only observeOn is specified, all operators will be executed on the current thread and only operators
below the observeOn will be switched to thread specified by the observeOn

 */

//Disposable disposable = input.map(this::getRepositories)
//        .subscribe(result -> compositeDisposable.add(result.subscribe(System.out::println)));
