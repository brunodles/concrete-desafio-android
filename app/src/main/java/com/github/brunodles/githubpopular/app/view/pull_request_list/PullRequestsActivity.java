package com.github.brunodles.githubpopular.app.view.pull_request_list;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;

import com.github.brunodles.githubpopular.api.GithubEndpoint;
import com.github.brunodles.githubpopular.api.dto.PullRequest;
import com.github.brunodles.githubpopular.api.dto.Repository;
import com.github.brunodles.githubpopular.app.R;
import com.github.brunodles.githubpopular.app.application.GithubApplication;
import com.github.brunodles.githubpopular.app.databinding.ActivityListPullRequestBinding;
import com.github.brunodles.githubpopular.app.databinding.NavigationDrawerLayoutBinding;
import com.github.brunodles.githubpopular.app.view.toolbar.ToolbarTipOffsetListener;
import com.github.brunodles.recyclerview.VerticalSpaceItemDecoration;
import com.github.brunodles.utils.LogRx;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.parceler.Parcels;

import java.util.ArrayList;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

import static com.github.brunodles.utils.DimensionUtils.fromDp;

/**
 * Created by bruno on 31/10/16.
 */

public class PullRequestsActivity extends RxAppCompatActivity {

    private static final String TAG = "PullRequestsActivity";
    public static final String STATE_LIST = "state_list";
    public static final String EXTRA_REPOSITORY = "EXTRA_REPOSITORY";

    private NavigationDrawerLayoutBinding navigationDrawer;
    private ActivityListPullRequestBinding binding;
    private PullRequestAdapter adapter;
    private CompositeSubscription subscriptions;
    private GithubEndpoint github;

    public static Intent newIntent(Context context, Repository repository) {
        Intent intent = new Intent(context, PullRequestsActivity.class);
        intent.putExtra(EXTRA_REPOSITORY, Parcels.wrap(repository));
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater = getLayoutInflater();
        navigationDrawer = NavigationDrawerLayoutBinding.inflate(layoutInflater);
        binding = ActivityListPullRequestBinding.inflate(layoutInflater, navigationDrawer.navigationContainer, true);
        setContentView(navigationDrawer.getRoot());

        setupToolbar(binding.toolbar);

        subscriptions = new CompositeSubscription();
        github = GithubApplication.githubApi();

        Intent intent = getIntent();
        Repository repository = Parcels.unwrap(intent.getParcelableExtra(EXTRA_REPOSITORY));

        binding.setRepository(repository);

        setupRecyclerView(binding.recyclerView);

        Subscription subscription = github.pullRequests(repository.owner.login, repository.name)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .subscribe(adapter::addList,
                        LogRx.e(TAG, "loadPage: failed to load pull requests"));
        subscriptions.add(subscription);


        lifecycle().filter(event -> event == ActivityEvent.DESTROY)
                .subscribe(e -> subscriptions.unsubscribe());
    }

    private void setupToolbar(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        adapter = new PullRequestAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);

        adapter.setUserProvider(github::user);
        adapter.setOnItemClickListener(this::onItemClick);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        VerticalSpaceItemDecoration itemDecoration = new VerticalSpaceItemDecoration(
                (int) fromDp(getResources(), 4));
        recyclerView.addItemDecoration(itemDecoration);
    }

    private void onItemClick(Integer integer, PullRequest pullRequest) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(pullRequest.html_url));
        startActivity(intent);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ArrayList<PullRequest> list = Parcels.unwrap(savedInstanceState.getParcelable(STATE_LIST));
        adapter.setList(list);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Parcelable wrap = Parcels.wrap(new ArrayList<>(adapter.getList()));
        outState.putParcelable(STATE_LIST, wrap);
    }
}
