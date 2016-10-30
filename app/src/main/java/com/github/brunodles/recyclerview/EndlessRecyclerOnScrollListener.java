package com.github.brunodles.recyclerview;

/**
 * Sample from https://gist.github.com/ssinss/e06f12ef66c51252563e
 */

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import rx.functions.Action1;

public final class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 5; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int current_page = 1;

    private final LinearLayoutManager mLinearLayoutManager;
    private final Action1<Integer> loader;

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager, Action1<Integer> loader) {
        this.mLinearLayoutManager = linearLayoutManager;
        this.loader = loader;

        if (firstVisibleItem == 0) loader.call(current_page++);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount)
                <= (firstVisibleItem + visibleThreshold)) {
            // End has been reached

            // Do something
            current_page++;

            loader.call(current_page);

            loading = true;
        }
    }
}