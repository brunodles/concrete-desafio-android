package com.github.brunodles.githubpopular.app.view;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.github.brunodles.githubpopular.app.R;

/**
 * Created by bruno on 31/10/16.
 */
public class ToolbarTipOffsetListener implements AppBarLayout.OnOffsetChangedListener, Animator.AnimatorListener {

    private final Toolbar toolbar;
    private final ImageView toolbarTip;
    private final int primary;
    private final int primaryDark;
    private boolean isAnimating = false;
    private int lastVerticalOffset;
    private int lastColor;

    public ToolbarTipOffsetListener(Toolbar toolbar, ImageView toolbarTip) {
        this.toolbar = toolbar;
        this.toolbarTip = toolbarTip;
        Context context = toolbar.getContext();
        primary = ContextCompat.getColor(context, R.color.colorPrimary);
        primaryDark = ContextCompat.getColor(context, R.color.colorPrimaryDark);
        lastColor = primary;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        lastVerticalOffset = verticalOffset;
        if (isAnimating) return;
        checkColor(verticalOffset);
    }

    private void checkColor(int verticalOffset) {
        int nextColor = colorFor(verticalOffset);
        if (lastColor != nextColor) animateFor(nextColor);
    }

    private int colorFor(int verticalOffset) {
        return shouldShowDarkColor(verticalOffset)
                ? primaryDark
                : primary;
    }

    private void animateFor(int nextColor) {
        ValueAnimator animator = buildAnimator(lastColor, nextColor);
        animator.setDuration(100);
        animator.addUpdateListener(animation -> toolbarTip.setColorFilter((int) animation.getAnimatedValue()));
        animator.addListener(this);
        animator.start();
        lastColor = nextColor;
    }

    private boolean shouldShowDarkColor(int verticalOffset) {
        return verticalOffset <= toolbar.getHeight() * -1;
    }

    private static ValueAnimator buildAnimator(int primaryDark, int primary) {
        return ValueAnimator.ofObject(new ArgbEvaluator(), primaryDark, primary);
    }

    @Override
    public void onAnimationStart(Animator animation) {
        isAnimating = true;
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        isAnimating = false;
        checkColor(lastVerticalOffset);
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
