package com.github.brunodles.githubpopular.app.view.pull_request_list;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.github.brunodles.adapter.RecyclerViewAdapter;
import com.github.brunodles.githubpopular.api.dto.PullRequest;
import com.github.brunodles.githubpopular.api.dto.Repository;
import com.github.brunodles.githubpopular.api.dto.User;
import com.github.brunodles.githubpopular.app.databinding.ItemPullRequestBinding;
import com.github.brunodles.utils.LogRx;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action2;
import rx.functions.Func1;

/**
 * Created by bruno on 30/10/16.
 */

public class PullRequestAdapter extends RecyclerViewAdapter<PullRequest, PullRequestAdapter.ViewHolder> {

    private static final String TAG = "RepositoryAdapter";

    private Func1<String, Observable<User>> provider;
    private Action2<Integer, PullRequest> listener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(ItemPullRequestBinding.inflate(inflater(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.cancelSubscription();
        PullRequest pullRequest = list.get(position);
        holder.binding.setUser(null);
        holder.binding.setPullRequest(pullRequest);
        if (provider != null) {
            holder.subscription = provider.call(pullRequest.user.login)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(holder.binding::setUser, LogRx.e(TAG, "onBindViewHolder: "));
        }
        if (listener != null)
            holder.itemView.setOnClickListener(v -> listener.call(holder.getAdapterPosition(), pullRequest));
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        holder.cancelSubscription();
    }

    public void setUserProvider(Func1<String, Observable<User>> provider) {
        this.provider = provider;
    }

    public void setOnItemClickListener(Action2<Integer, PullRequest> listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final ItemPullRequestBinding binding;
        private Subscription subscription;

        public ViewHolder(ItemPullRequestBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void cancelSubscription() {
            if (subscription != null) {
                subscription.unsubscribe();
            }
        }
    }
}
