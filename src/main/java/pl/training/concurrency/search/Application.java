package pl.training.concurrency.search;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.List;

public class Application {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private Retrofit createRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private void start() {
        Runtime.getRuntime().addShutdownHook(new Thread(compositeDisposable::dispose));
        GithubService githubService = new GithubService(createRetrofit());

        Observable<List<Repository>> repositories = githubService.getRepositoriesSummary("landrzejewski");
        compositeDisposable.add(repositories.subscribe(System.out::println));
    }

    public static void main(String[] args) {
        new Application().start();
    }

}
