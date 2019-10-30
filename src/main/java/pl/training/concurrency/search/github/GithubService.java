package pl.training.concurrency.search.github;

import io.reactivex.Observable;
import retrofit2.Retrofit;

import java.util.List;

public class GithubService {

    private final GithubApi githubApi;

    public GithubService(Retrofit retrofit) {
        githubApi = retrofit.create(GithubApi.class);
    }

    public Observable<List<Repository>> getUserRepositories(String username) {
        return githubApi.getUserRepositories(username);
    }

    public Observable<List<Repository>> getRepositories(String query) {
        System.out.println("Get repositories...");
        return githubApi.getRepositories(query)
                .map(QueryResult::getItems);
    }

}
