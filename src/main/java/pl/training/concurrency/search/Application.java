package pl.training.concurrency.search;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.List;

public class Application {

    public static void main(String[] args) {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        Runtime.getRuntime().addShutdownHook(new Thread(compositeDisposable::dispose));
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        GithubService githubService = new GithubService(retrofit);

        Observable<List<Repository>> repositories = githubService.getRepositoriesSummary("landrzejewski");
        compositeDisposable.add(repositories.subscribe(System.out::println));
    }

}
