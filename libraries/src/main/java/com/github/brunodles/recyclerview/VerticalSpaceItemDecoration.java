package com.github.brunodles.recyclerview;

import android.graphics.Rect;
import android.support.annotation.Dimension;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import static android.support.annotation.Dimension.PX;

/**
 * Got from http://stackoverflow.com/questions/24618829/how-to-add-dividers-and-spaces-between-items-in-recyclerview
 */

public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int verticalSpaceHeight;

    public VerticalSpaceItemDecoration(@Dimension(unit = PX) int verticalSpaceHeight) {
        this.verticalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.bottom = verticalSpaceHeight;
    }
}