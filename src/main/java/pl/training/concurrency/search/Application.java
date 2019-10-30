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

    private List<String> combine(List<String> firstList, List<String> secondList) {
        List<String> result = new ArrayList<>();
        result.addAll(firstList);
        result.addAll(secondList);
        return result;
    }

    private void start() throws InterruptedException {
        Runtime.getRuntime().addShutdownHook(new Thread(compositeDisposable::dispose));

        Observable<String> input = ObservableReader.from(System.in)
                .subscribeOn(Schedulers.io())
                .share();

        Observable<List<Repository>> repositories = input.flatMap(query -> githubService.getRepositories(query));
        Observable<List<Article>> articles = input.flatMap(query -> wikipediaService.getArticles(query));

        Observable<String> repositoriesNames = repositories
                .subscribeOn(Schedulers.io())
                .flatMap(Observable::fromIterable)
                .map(Repository::getName);

        Observable<String> articlesNames = articles
                .subscribeOn(Schedulers.io())
                .flatMap(Observable::fromIterable)
                .map(Article::getTitle);

        Observable<List<String>> results = Observable.combineLatest(repositoriesNames, articlesNames, List::of)
                .doOnNext(this::showThreadInfo)
                .take(5)
                .reduce(new ArrayList<>(), this::combine)
                .observeOn(Schedulers.io())
                .toObservable();

        compositeDisposable.add(results.subscribe(System.out::println));

        Thread.sleep(100_000);
    }

    public static void main(String[] args) throws InterruptedException {
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
