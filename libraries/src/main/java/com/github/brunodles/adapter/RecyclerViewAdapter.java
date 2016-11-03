package com.github.brunodles.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.github.brunodles.utils.LogRx;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import rx.Observable;

/**
 * Created by bruno on 31/10/16.
 */
public abstract class RecyclerViewAdapter<TYPE, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {
    private static final String TAG = "RecyclerViewAdapter";

    protected final List<TYPE> items = new LinkedList<TYPE>();
    protected WeakReference<LayoutInflater> layoutInflater;

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItems(List<TYPE> list) {
        int position = this.items.size();
        Observable.from(list)
                .filter(o -> !this.items.contains(o))
                .toList()
                .subscribe(objects -> {
                    this.items.addAll(objects);
                    notifyItemRangeInserted(position, objects.size());
                }, LogRx.e(TAG, "addItems: failed to add items into the list"));
    }

    public void setItems(List<TYPE> items) {
        this.items.clear();
        addItems(items);
    }

    /**
     * @return a unmodifiable items of the items
     */
    public List<TYPE> getItems() {
        return Collections.unmodifiableList(new ArrayList<TYPE>(items));
    }

    protected LayoutInflater inflater(Context context) {
        LayoutInflater inflater;
        if (layoutInflater == null || (inflater = layoutInflater.get()) == null) {
            inflater = LayoutInflater.from(context);
            layoutInflater = new WeakReference<>(inflater);
            return inflater;
        }
        return inflater;
    }
}
