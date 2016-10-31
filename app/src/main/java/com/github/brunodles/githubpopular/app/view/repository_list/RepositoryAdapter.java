package com.github.brunodles.githubpopular.app.view.repository_list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.github.brunodles.githubpopular.api.dto.Repository;
import com.github.brunodles.githubpopular.api.dto.User;
import com.github.brunodles.githubpopular.app.databinding.ItemRepositoryBinding;
import com.github.brunodles.utils.LogRx;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action2;
import rx.functions.Func1;

/**
 * Created by bruno on 30/10/16.
 */

public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.ViewHolder> {

    private static final String TAG = "RepositoryAdapter";

    private WeakReference<LayoutInflater> layoutInflater;
    private final List<Repository> list = new ArrayList<>();
    private Func1<String, Observable<User>> provider;
    private Action2<Integer, Repository> listener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(ItemRepositoryBinding.inflate(inflator(parent.getContext())));
    }

    private LayoutInflater inflator(Context context) {
        LayoutInflater inflater;
        if (layoutInflater == null || (inflater = layoutInflater.get()) == null) {
            inflater = LayoutInflater.from(context);
            layoutInflater = new WeakReference<>(inflater);
            return inflater;
        }
        return inflater;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.cancelSubscription();
        Repository repository = list.get(position);
        holder.binding.setUser(null);
        holder.binding.setRepository(repository);
        if (provider != null) {
            holder.subscription = provider.call(repository.owner.login)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(holder.binding::setUser, LogRx.e(TAG, "onBindViewHolder: "));
        }
        if (listener != null)
            holder.itemView.setOnClickListener(v -> listener.call(holder.getAdapterPosition(), repository));
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        holder.cancelSubscription();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addList(List<Repository> list) {
        int position = this.list.size();
        this.list.addAll(list);
        notifyItemRangeInserted(position, list.size());
    }

    public void setList(List<Repository> list) {
        this.list.clear();
        addList(list);
    }

    /**
     * @return a unmodifiable list of the items
     */
    public List<Repository> getList() {
        return Collections.unmodifiableList(list);
    }

    public void setUserProvider(Func1<String, Observable<User>> provider) {
        this.provider = provider;
    }

    public void setOnItemClickListener(Action2<Integer, Repository> listener) {
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
