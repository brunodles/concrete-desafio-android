package com.github.brunodles.githubpopular.app.application;

import android.support.annotation.NonNull;

import com.github.brunodles.githubpopular.api.GithubEndpoint;
import com.github.brunodles.githubpopular.api.dto.PullRequest;
import com.github.brunodles.githubpopular.api.dto.Repository;
import com.github.brunodles.githubpopular.api.dto.SearchEvenlope;
import com.github.brunodles.githubpopular.api.dto.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by bruno on 03/11/16.
 */

public class Factory {

    public static final String PULL_URL = "https://github.com/brunodles/java-validation/pulls";
    public static final String REPOSITORY_NAME = "java-validations";

    public static GithubEndpoint mockedGithub() {
        return new GithubEndpoint() {
            @Override
            public Observable<SearchEvenlope> searchRepositories(@Query("q") String query, @Query("sort") String sort, @Query("page") int page) {
                return Observable.just(searchEvenlop());
            }

            @Override
            public Observable<List<PullRequest>> pullRequests(@Path("owner") String owner, @Path("repository") String repository) {
                return Observable.just(pullRequest()).toList();
            }

            @Override
            public Observable<User> user(@Path("username") String owner) {
                return Observable.just(Factory.user());
            }
        };
    }

    private static SearchEvenlope searchEvenlop() {
        SearchEvenlope searchEvenlope = new SearchEvenlope();
        searchEvenlope.items = new ArrayList<>();
        searchEvenlope.items.add(repository());
        return searchEvenlope;
    }

    @NonNull
    public static PullRequest pullRequest() {
        PullRequest pullRequest = new PullRequest();
        pullRequest.html_url = "https://github.com/brunodles/java-validation/pull/11";
        pullRequest.title = "Add not matcher method";
        pullRequest.body = "<!-- Reviewable:start -->\\n\\nThis change is [<img src=\\\"https://reviewable.io/review_button.svg\\\" height=\\\"35\\\" align=\\\"absmiddle\\\" alt=\\\"Reviewable\\\"/>](https://reviewable.io/reviews/brunodles/java-validation/11)\\n\\n<!-- Reviewable:end -->\\n";
        pullRequest.user = user();
        return pullRequest;
    }

    @NonNull
    public static Repository repository() {
        Repository repository = new Repository();
        repository.name = REPOSITORY_NAME;
        repository.full_name = "brunodles/java-validations";
        repository.owner = user();
        repository.description = "A lib to make validations on java objects";
        repository.open_issues = 8L;
        repository.isPrivate = false;
        repository.pulls_url = PULL_URL;
        return repository;
    }

    @NonNull
    public static User user() {
        User user = new User();
        user.login = "brunodles";
        user.avatar_url = "https://avatars.githubusercontent.com/u/4053904?v=3";
        user.name = "Bruno de Lima e Silva";
        return user;
    }
}
