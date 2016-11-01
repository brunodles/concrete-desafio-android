package com.github.brunodles.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by bruno on 31/10/16.
 */
public abstract class RecyclerViewAdapter<TYPE, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    protected final List<TYPE> list = new ArrayList<>();
    private WeakReference<LayoutInflater> layoutInflater;

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addList(List<TYPE> list) {
        int position = this.list.size();
        this.list.addAll(list);
        notifyItemRangeInserted(position, list.size());
    }

    public void setList(List<TYPE> list) {
        this.list.clear();
        addList(list);
    }

    /**
     * @return a unmodifiable list of the items
     */
    public List<TYPE> getList() {
        return Collections.unmodifiableList(list);
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
