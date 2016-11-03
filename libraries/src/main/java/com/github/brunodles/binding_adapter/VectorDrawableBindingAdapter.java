package com.github.brunodles.binding_adapter;

import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.view.View;

/**
 * Created by bruno on 01/11/16.
 */

public class VectorDrawableBindingAdapter {

    @BindingAdapter("backgroundCompat")
    public static void setBackgroundCompat(View view, int backgroundCompat) {
        Resources resources = view.getResources();
        Resources.Theme theme = view.getContext().getTheme();
        Drawable drawable = VectorDrawableCompat.create(resources, backgroundCompat, theme);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }
}
