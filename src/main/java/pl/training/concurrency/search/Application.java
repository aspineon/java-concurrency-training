package pl.training.concurrency.search;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import pl.training.concurrency.search.github.GithubService;
import pl.training.concurrency.search.github.Repository;
import pl.training.concurrency.search.wikipedia.Article;
import pl.training.concurrency.search.wikipedia.WikipediaService;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Application {

    private final GithubService githubService = new GithubService(retrofitBuilder("https://api.github.com/"));
    private final WikipediaService wikipediaService = new WikipediaService(retrofitBuilder("https://en.wikipedia.org/w/"));
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private Retrofit retrofitBuilder(String url) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor().setLevel(Level.BASIC)).build())
                .build();
    }

    private <T> void showThreadInfo(T input) {
        System.out.println("processing item on thread " + Thread.currentThread().getName());
    }

    private List<String> combine(List<String> result, String value) {
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

        Observable<List<String>> namePairs = Observable.zip(repositoriesNames, articlesNames, List::of)
                .flatMap(Observable::fromIterable)
                .map(String::toLowerCase)
                .filter(this::hasWhiteSpaces)
                .distinct()
                .reduce(new ArrayList<>(), this::combine)
                .toObservable();

        compositeDisposable.add(namePairs.subscribe(System.out::println, System.out::println, () -> System.out.println("Completed")));
    }

    private Observable<List<Article>> getRepositories(String query) {
        Observable<List<Article>> articles = githubService.getRepositories(query)
                .flatMap(Observable::fromIterable)
                .firstElement()
                .toObservable()
                .map(Repository::getName)
                .flatMap(name -> wikipediaService.getArticles(query))
                .share();

        compositeDisposable.add(articles.subscribeOn(Schedulers.io()).subscribe(System.out::println));
        return articles;
    }

    private void errors() {
        Observable<Integer> numbers = Observable.just(0, 1, 2)
                .subscribeOn(Schedulers.io())
                .map(result -> 1 / result)
                //.onErrorReturn(throwable -> -1)
                //.onErrorReturnItem(-2)
                // .onErrorReturnItem(-1)
                .retry(3)
                .share();

        compositeDisposable.add(numbers.subscribe(System.out::println, System.err::println, () -> System.out.println("Completed")));
        compositeDisposable.add(numbers.subscribe(System.out::println, System.err::println, () -> System.out.println("Completed")));
    }

    private Observable<Long> safeStream() {
        Random random = new Random();
        PublishSubject<Long> publishSubject = PublishSubject.create();

        Observable<Long> ticks = Observable.interval(5, TimeUnit.SECONDS);
        compositeDisposable.add(ticks.subscribe((value) -> {
            Observable<Boolean> numbers = Observable.just(random.nextBoolean());
            compositeDisposable.add(numbers.map(lucky -> {
                if (lucky) {
                    return 5L;
                }
                throw new IllegalArgumentException();
            }).subscribe(publishSubject::onNext, error -> publishSubject.onNext(-1L), () -> System.out.println("Completed")));
        }));
        return publishSubject;
    }

    private void start() throws InterruptedException {
        Runtime.getRuntime().addShutdownHook(new Thread(compositeDisposable::dispose));
        //compositeDisposable.add(ObservableReader.from(System.in).subscribe(this::sendQuery));

        //Observable<List<Article>> articles = getRepositories("java").subscribeOn(Schedulers.io());
        //compositeDisposable.add(articles.subscribe(System.out::println));

        //errors();

        compositeDisposable.add(safeStream().subscribe(System.out::println));
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
