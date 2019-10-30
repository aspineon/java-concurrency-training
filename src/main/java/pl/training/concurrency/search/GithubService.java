package pl.training.concurrency.search;

import io.reactivex.Observable;
import retrofit2.Retrofit;

import java.util.List;
import java.util.logging.Logger;

public class GithubService {

    private final Logger logger = Logger.getLogger(getClass().getName());

    private GithubApi githubApi;

    public GithubService(Retrofit retrofit) {
        githubApi = retrofit.create(GithubApi.class);
    }

    public Observable<List<Repository>> getRepositoriesSummary(String username) {
        return githubApi.repositories(username);
    }

}
