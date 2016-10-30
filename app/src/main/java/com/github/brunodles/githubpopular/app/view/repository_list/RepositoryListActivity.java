package com.github.brunodles.githubpopular.app.view.repository_list;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.brunodles.githubpopular.api.Api;
import com.github.brunodles.githubpopular.api.GithubEndpoint;
import com.github.brunodles.githubpopular.app.BuildConfig;
import com.github.brunodles.githubpopular.app.R;
import com.github.brunodles.githubpopular.app.databinding.ActivityListRepositoryBinding;
import com.github.brunodles.recyclerview.VerticalSpaceItemDecoration;
import com.github.brunodles.utils.LogRx;

import rx.android.schedulers.AndroidSchedulers;

import static com.github.brunodles.utils.DimensionUtils.fromDp;


/**
 * Created by bruno on 29/10/16.
 */

public class RepositoryListActivity extends AppCompatActivity {

    private static final String TAG = "RepositoryListActivity";

    private ActivityListRepositoryBinding binding;
    private RepositoryAdapter repositoryAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list_repository);
        binding.toolbar.title.setText("Java Pop");

        setupRecyclerView(binding.recyclerView);

        GithubEndpoint github = new Api(BuildConfig.API_URL, getCacheDir()).github();
        github.searchRepositories("language:Java", "star", 1)
                .observeOn(AndroidSchedulers.mainThread())
                .map(e -> e.items)
                .subscribe(repositoryAdapter::addList,
                        LogRx.e(TAG, "onCreate: "));

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
    }
}
