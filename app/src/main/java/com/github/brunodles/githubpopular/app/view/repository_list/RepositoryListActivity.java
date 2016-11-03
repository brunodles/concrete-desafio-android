package com.github.brunodles.githubpopular.app.view.repository_list;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;

import com.github.brunodles.githubpopular.api.dto.Repository;
import com.github.brunodles.githubpopular.api.dto.User;
import com.github.brunodles.githubpopular.app.R;
import com.github.brunodles.githubpopular.app.databinding.ActivityListRepositoryBinding;
import com.github.brunodles.githubpopular.app.databinding.ItemRepositoryBinding;
import com.github.brunodles.githubpopular.app.databinding.NavigationDrawerLayoutBinding;
import com.github.brunodles.githubpopular.app.view.pull_request_list.PullRequestsActivity;
import com.github.brunodles.githubpopular.app.view.toolbar.ToolbarTipOffsetListener;
import com.github.brunodles.recyclerview.EndlessRecyclerOnScrollListener;
import com.github.brunodles.recyclerview.VerticalSpaceItemDecoration;
import com.github.brunodles.utils.LogRx;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.parceler.Parcels;

import java.util.ArrayList;

import hugo.weaving.DebugLog;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

import static com.github.brunodles.githubpopular.app.application.GithubApplication.githubApi;
import static com.github.brunodles.utils.DimensionUtils.fromDp;


/**
 * Created by bruno on 29/10/16.
 */

public class RepositoryListActivity extends RxAppCompatActivity {

    private static final String TAG = "RepositoryListActivity";
    public static final String STATE_LIST = "state_list";
    private static final String STATE_PAGE = "state_page";

    private NavigationDrawerLayoutBinding navigationDrawer;
    private ActivityListRepositoryBinding binding;
    private RepositoryAdapter repositoryAdapter;
    private CompositeSubscription subscriptions;
    private LinearLayoutManager linearLayoutManager;
    private EndlessRecyclerOnScrollListener scrollListener;

    @DebugLog
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater = getLayoutInflater();
        navigationDrawer = NavigationDrawerLayoutBinding.inflate(layoutInflater);
        binding = ActivityListRepositoryBinding.inflate(layoutInflater, navigationDrawer.navigationContainer, true);
        setContentView(navigationDrawer.getRoot());

        binding.toolbar.setNavigationIcon(R.drawable.ic_menu);

        subscriptions = new CompositeSubscription();

        setupRecyclerView(binding.recyclerView);

        if (savedInstanceState != null) {
            scrollListener.setNextPage(savedInstanceState.getInt(STATE_PAGE, 1));
        }
    }

    @DebugLog
    private void setupRecyclerView(RecyclerView recyclerView) {
        repositoryAdapter = new RepositoryAdapter();
        linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);

        recyclerView.setAdapter(repositoryAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        VerticalSpaceItemDecoration itemDecoration = new VerticalSpaceItemDecoration(
                (int) fromDp(getResources(), 4));
        recyclerView.addItemDecoration(itemDecoration);

        scrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager, this::loadPage)
                .setVisibleThreshold(10);
    }

    @Override
    protected void onStart() {
        super.onStart();
        repositoryAdapter.setUserProvider(this::getUser);
        scrollListener.loadFirstIfNeeded();
    }

    private void loadPage(int page) {
        Log.d(TAG, "loadPage: " + page);
        Subscription subscription = githubApi().searchRepositories("language:Java", "star", page)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .map(e -> e.items)
                .subscribe(repositoryAdapter::addItems,
                        LogRx.e(TAG, "loadPage: failed to load page " + page),
                        () -> Log.d(TAG, "loadPage: adapter itemcount " + repositoryAdapter.getItemCount()));
        subscriptions.add(subscription);
    }

    @DebugLog
    @Override
    protected void onResume() {
        super.onResume();
        binding.appbar.addOnOffsetChangedListener(ToolbarTipOffsetListener.color(binding.toolbar, binding.toolbarTip));
        repositoryAdapter.setOnItemClickListener(this::onItemClick);
        binding.recyclerView.addOnScrollListener(scrollListener);
        binding.toolbar.setNavigationOnClickListener(v ->
                navigationDrawer.navigationLayout.openDrawer(
                        navigationDrawer.navigationItemsInclude.navigationItems)
        );
    }

    private void onItemClick(ItemRepositoryBinding binding, Repository repository) {
        Intent intent = PullRequestsActivity.newIntent(this, repository);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                binding.userImage, "userImage");
        ActivityCompat.startActivity(this, intent, options.toBundle());
    }

    private Observable<User> getUser(String s) {
        return githubApi().user(s)
                .compose(bindToLifecycle());
    }

    @DebugLog
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState() called with: savedInstanceState");
        ArrayList<Repository> list = Parcels.unwrap(savedInstanceState.getParcelable(STATE_LIST));
        repositoryAdapter.setItems(list);
        scrollListener.setNextPage(savedInstanceState.getInt(STATE_PAGE));
    }

    @DebugLog
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        Log.d(TAG, "onRestoreInstanceState() called with: savedInstanceState, persistentState");
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        ArrayList<Repository> list = Parcels.unwrap(savedInstanceState.getParcelable(STATE_LIST));
        repositoryAdapter.setItems(list);
        scrollListener.setNextPage(savedInstanceState.getInt(STATE_PAGE));
    }

    @DebugLog
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Parcelable wrap = Parcels.wrap(new ArrayList<>(repositoryAdapter.getItems()));
        outState.putParcelable(STATE_LIST, wrap);
        outState.putInt(STATE_PAGE, scrollListener.getNextPage());
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.recyclerView.removeOnScrollListener(scrollListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscriptions.unsubscribe();
    }
}