package com.github.brunodles.githubpopular.api;

import com.github.brunodles.githubpopular.api.dto.PullRequest;
import com.github.brunodles.githubpopular.api.dto.SearchEvenlope;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by bruno on 29/10/16.
 */

public interface GithubEndpoint {

    @GET("search/repositories")
    Observable<SearchEvenlope> searchRepositories(
            @Query("q") String query,
            @Query("sort") String sort,
            @Query("page") int page
    );

    @GET("repos/{owner}/{repository}/pulls")
    Observable<List<PullRequest>> repositoryPulls(
            @Path("owner") String owner,
            @Path("reposotory") String repository
    );
}
