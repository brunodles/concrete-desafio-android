package com.github.brunodles.githubpopular.app.view.toolbar;

import android.animation.Animator;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.github.brunodles.githubpopular.app.R;

/**
 * Created by bruno on 31/10/16.
 */

public interface ToolbarTipOffsetListener extends AppBarLayout.OnOffsetChangedListener, Animator.AnimatorListener {

    public static ToolbarTipOffsetListener color(Toolbar toolbar, ImageView toolbarTip) {
        return new ToolbarTipOffsetListener_color(toolbar, toolbarTip, R.color.colorPrimary,
                R.color.colorPrimaryDark);
    }

    public static ToolbarTipOffsetListener transparent(Toolbar toolbar, ImageView toolbarTip) {
        return new ToolbarTipOffsetListener_color(toolbar, toolbarTip, R.color.colorPrimary,
                android.R.color.transparent);
    }
}
