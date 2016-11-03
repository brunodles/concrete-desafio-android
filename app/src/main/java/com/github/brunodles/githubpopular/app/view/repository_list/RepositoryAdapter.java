package com.github.brunodles.githubpopular.app.view.repository_list;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.github.brunodles.adapter.RecyclerViewAdapter;
import com.github.brunodles.githubpopular.api.dto.Repository;
import com.github.brunodles.githubpopular.api.dto.User;
import com.github.brunodles.githubpopular.app.databinding.ItemRepositoryBinding;
import com.github.brunodles.utils.LogRx;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action2;
import rx.functions.Func1;

/**
 * Created by bruno on 30/10/16.
 */

public class RepositoryAdapter extends RecyclerViewAdapter<Repository, RepositoryAdapter.ViewHolder> {

    private static final String TAG = "RepositoryAdapter";

    private Func1<String, Observable<User>> provider;
    private Action2<ItemRepositoryBinding, Repository> listener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(ItemRepositoryBinding.inflate(inflater(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.cancelSubscription();
        Repository repository = items.get(position);
        holder.binding.setUser(null);
        holder.binding.setRepository(repository);
        if (provider != null) {
            holder.subscription = provider.call(repository.owner.login)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(holder.binding::setUser, LogRx.e(TAG, "onBindViewHolder: "));
        }
        if (listener != null)
            holder.itemView.setOnClickListener(v -> listener.call(holder.binding, repository));
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        holder.cancelSubscription();
    }

    public void setUserProvider(Func1<String, Observable<User>> provider) {
        this.provider = provider;
    }

    public void setOnItemClickListener(Action2<ItemRepositoryBinding, Repository> listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final ItemRepositoryBinding binding;
        private Subscription subscription;

        public ViewHolder(ItemRepositoryBinding binding) {
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
