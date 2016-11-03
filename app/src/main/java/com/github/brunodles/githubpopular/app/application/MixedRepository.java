package com.github.brunodles.githubpopular.app.application;

import android.support.annotation.NonNull;
import android.util.Log;

import com.github.brunodles.githubpopular.api.GithubEndpoint;
import com.github.brunodles.githubpopular.api.dto.PullRequest;
import com.github.brunodles.githubpopular.api.dto.SearchEvenlope;
import com.github.brunodles.githubpopular.api.dto.User;
import com.github.brunodles.githubpopular.app.data.UserTable;
import com.github.brunodles.utils.LogRx;
import com.squareup.sqlbrite.BriteDatabase;

import java.util.List;

import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by bruno on 02/11/16.
 */
public class MixedRepository implements GithubEndpoint {
    private static final String TAG = "MixedRepository";

    private final GithubEndpoint api;
    private final BriteDatabase database;

    public MixedRepository(GithubEndpoint api, BriteDatabase database) {
        this.api = api;
        this.database = database;
    }

    @Override
    public Observable<SearchEvenlope> searchRepositories(
            @Query("q") String query, @Query("sort") String sort, @Query("page") int page) {
        return api.searchRepositories(query, sort, page);
    }

    @Override
    public Observable<List<PullRequest>> pullRequests(
            @Path("owner") String owner, @Path("repository") String repository) {
        return api.pullRequests(owner, repository);
    }

    @Override
    public Observable<User> user(@Path("username") String owner) {
        Log.d(TAG, "getUser: " + owner);
        return UserTable.find(database, owner)
                .doOnError(LogRx.e(TAG, "user: failed to get user (" + owner + ") from database "))
                .map(errorOnNull())
                .doOnCompleted(() -> Log.d(TAG, "finished the query for " + owner))
                .switchIfEmpty(userFromApi(owner))
                .onErrorResumeNext(userFromApi(owner));
    }

    private <T> Func1<T, T> errorOnNull() {
        return t -> {
            if (t == null)
                throw new NullPointerException();
            return t;
        };
    }

    @NonNull
    private Observable<User> userFromApi(@Path("username") String owner) {
        return api.user(owner)
                .doOnNext(user -> UserTable.insert(database, user))
                .doOnNext(u -> Log.d(TAG, "updated user (" + owner + ") database with " + u.login))
                .doOnError(LogRx.e(TAG, "failed to get the user(" + owner + ") from api "));
    }
}
