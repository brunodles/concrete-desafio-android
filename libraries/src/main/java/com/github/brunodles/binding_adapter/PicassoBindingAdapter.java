package com.github.brunodles.binding_adapter;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

/**
 * Created by bruno on 30/10/16.
 */

public class PicassoBindingAdapter {
    private static final String TAG = "PicassoBindingAdapter";

    @BindingAdapter(value = {"android:src", "placeHolder"}, requireAll = false)
    public static void setImageUrl(ImageView view, String url, int placeHolder) {
        RequestCreator requestCreator =
                Picasso.with(view.getContext()).load(url);
        if (placeHolder != 0)
            requestCreator.placeholder(placeHolder);
        requestCreator.into(view);
    }
}
