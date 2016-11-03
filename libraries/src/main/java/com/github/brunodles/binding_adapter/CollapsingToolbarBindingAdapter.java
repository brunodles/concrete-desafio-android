package com.github.brunodles.binding_adapter;

import android.databinding.BindingAdapter;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.util.Log;
import android.view.ViewParent;

/**
 * Created by bruno on 01/11/16.
 */
public final class CollapsingToolbarBindingAdapter {
    private static final String TAG = "CollapsingToolbarBindin";

    @BindingAdapter({"expandedTitle", "collapsedTitle"})
    public static void setExpandedTitle(CollapsingToolbarLayout view, String expandedTitle, String collapsedTitle) {
        Log.d(TAG, "setExpandedTitle() called with: collapsedTitle = [" + collapsedTitle + "], expandedTitle = [" + expandedTitle + "]");
        ViewParent parent = view.getParent();
        if (parent instanceof AppBarLayout)
            ((AppBarLayout) parent).addOnOffsetChangedListener(
                    new AppBarListener(view, collapsedTitle, expandedTitle));

    }

//    @BindingAdapter("expandedTitle")
//    public static void setExpandedTitle(CollapsingToolbarLayout view, String expandedTitle) {
//        CharSequence collapsedTitle = view.getTitle();
//
//        setExpandedTitle(view, expandedTitle, collapsedTitle);
//    }

    private static class AppBarListener implements AppBarLayout.OnOffsetChangedListener {
        private final CollapsingToolbarLayout view;
        private String collapsedTitle;
        private final String expandedTitle;
        private boolean isShow = false;
        private int scrollRange = -1;

        public AppBarListener(CollapsingToolbarLayout view, CharSequence collapsedTitle, String expandedTitle) {
            this.view = view;
            setCollapsedTitle(collapsedTitle);
            this.expandedTitle = expandedTitle;
        }

        private void setCollapsedTitle(CharSequence title) {
            if (title != null) this.collapsedTitle = title.toString();
        }

        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            if (scrollRange == -1) {
                scrollRange = appBarLayout.getTotalScrollRange();
            }
            if (scrollRange + verticalOffset == 0) {
                view.setTitle(collapsedTitle);
                isShow = true;
            } else if (isShow) {
                if (collapsedTitle == null) setCollapsedTitle(view.getTitle());
                view.setTitle(expandedTitle);
                isShow = false;
            }
        }
    }
}
