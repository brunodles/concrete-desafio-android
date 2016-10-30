package com.github.brunodles.githubpopular.api;

import com.github.brunodles.githubpopular.api.dto.PullRequest;
import com.github.brunodles.githubpopular.api.dto.SearchEvenlope;
import com.github.brunodles.githubpopular.api.dto.User;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by bruno on 29/10/16.
 */

public interface GithubEndpoint {

    @Headers({"UserShort-Agent: Github-Popular"})
    @GET("search/repositories")
    Observable<SearchEvenlope> searchRepositories(
            @Query("q") String query,
            @Query("sort") String sort,
            @Query("page") int page
    );

    @Headers({"UserShort-Agent: Github-Popular"})
    @GET("repos/{owner}/{repository}/pulls")
    Observable<List<PullRequest>> pullRequests(
            @Path("owner") String owner,
            @Path("repository") String repository
    );

    @Headers({"UserShort-Agent: Github-Popular"})
    @GET("users/{username}")
    Observable<User> user(@Path("username") String owner);
}
