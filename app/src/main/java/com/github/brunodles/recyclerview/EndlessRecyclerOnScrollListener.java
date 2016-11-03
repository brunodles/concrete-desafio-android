package com.github.brunodles.recyclerview;

/**
 * Sample from https://gist.github.com/ssinss/e06f12ef66c51252563e
 */

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import rx.functions.Action1;

public final class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    private static final String TAG = "EndlessRecyclerOnScroll";

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 10; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int nextPage = 1;

    private final LinearLayoutManager mLinearLayoutManager;
    private final Action1<Integer> loader;

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager, Action1<Integer> loader) {
        this.mLinearLayoutManager = linearLayoutManager;
        this.loader = loader;
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
            load();
        }
    }

    public void load() {
        loader.call(nextPage++);
        loading = true;
    }

    public EndlessRecyclerOnScrollListener setVisibleThreshold(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
        return this;
    }

    public EndlessRecyclerOnScrollListener setNextPage(int nextPage) {
        Log.d(TAG, "setNextPage() called with: nextPage = [" + nextPage + "]");
        this.nextPage = nextPage;
        return this;
    }

    public int getNextPage() {
        Log.d(TAG, "getNextPage() returned: " + nextPage);
        return nextPage;
    }

    public void loadFirstIfNeeded() {
        if (nextPage == 1) load();
    }
}