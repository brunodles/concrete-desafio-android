package com.github.brunodles.githubpopular.app.view.repository_list;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.github.brunodles.githubpopular.api.Api;
import com.github.brunodles.githubpopular.api.GithubEndpoint;
import com.github.brunodles.githubpopular.app.BuildConfig;
import com.github.brunodles.githubpopular.app.R;
import com.github.brunodles.githubpopular.app.databinding.ActivityListRepositoryBinding;
import com.github.brunodles.recyclerview.EndlessRecyclerOnScrollListener;
import com.github.brunodles.recyclerview.VerticalSpaceItemDecoration;
import com.github.brunodles.utils.LogRx;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

import static com.github.brunodles.utils.DimensionUtils.fromDp;


/**
 * Created by bruno on 29/10/16.
 */

public class RepositoryListActivity extends RxAppCompatActivity {

    private static final String TAG = "RepositoryListActivity";

    private ActivityListRepositoryBinding binding;
    private RepositoryAdapter repositoryAdapter;
    private CompositeSubscription subscriptions;
    private GithubEndpoint github;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list_repository);
        binding.toolbar.title.setText("Java Pop");

        subscriptions = new CompositeSubscription();
        github = new Api(BuildConfig.API_URL, getCacheDir()).github();

        setupRecyclerView(binding.recyclerView);

        repositoryAdapter.setUserProvider(github::user);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        repositoryAdapter = new RepositoryAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);

        recyclerView.setAdapter(repositoryAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        VerticalSpaceItemDecoration itemDecoration = new VerticalSpaceItemDecoration(
                (int) fromDp(getResources(), 4));
        recyclerView.addItemDecoration(itemDecoration);

        EndlessRecyclerOnScrollListener scrollListener =
                new EndlessRecyclerOnScrollListener(linearLayoutManager, this::loadPage);
        recyclerView.addOnScrollListener(scrollListener);
    }

    private void loadPage(int page) {
        Log.d(TAG, "loadPage: " + page);
        Subscription subscription = github.searchRepositories("language:Java", "star", page)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .map(e -> e.items)
                .subscribe(repositoryAdapter::addList,
                        LogRx.e(TAG, "onCreate: "));
        subscriptions.add(subscription);
    }
}
