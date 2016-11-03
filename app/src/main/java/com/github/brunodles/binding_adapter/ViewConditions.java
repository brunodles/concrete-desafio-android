package com.github.brunodles.binding_adapter;

import android.databinding.BindingAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.github.brunodles.githubpopular.app.R;

/**
 * Created by bruno on 02/11/16.
 */

public class ViewConditions {

    @BindingAdapter(value = {"noHeightIf", "defaultHeight"}, requireAll = false)
    public static void noHeightIf(View view, boolean condition, int defaultHeight) {
        if (condition) {
            setHeight(view, 0);
        } else {
            Object tag = view.getTag(R.id.tag_previous_height);
            if (tag != null && Integer.TYPE.isInstance(tag))
                setHeight(view, (Integer) tag);
            else if (defaultHeight > 0)
                setHeight(view, defaultHeight);
        }
    }

    private static void setHeight(View view, int height) {
        int oldHeight = view.getHeight();
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = height;
        view.setLayoutParams(params);
        view.setTag(R.id.tag_previous_height, oldHeight);
    }
}
