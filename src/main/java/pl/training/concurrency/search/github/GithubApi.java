package pl.training.concurrency.search.github;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface GithubApi {

    @GET("users/{username}/repos")
    Observable<List<Repository>> repositories(@Path("username") String username);

}
