package com.github.brunodles.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.AnimRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.RequestCreator;

import java.io.IOException;

import rx.functions.Action0;

/**
 * Created by bruno on 13/06/16.
 */

public final class Picasso {

    public static boolean debug = false;

    private Picasso() {
    }

    @NonNull
    public static com.squareup.picasso.Picasso with(ImageView view) {
        return with(view.getContext());
    }

    public static RequestCreator load(ImageView view, String url) {
        return with(view).load(url);
    }

    @MainThread
    public static void loadInto(ImageView view, String url) {
        if (url == null || url.isEmpty())
            return;
        with(view).load(url).into(view);
    }

    @NonNull
    public static com.squareup.picasso.Picasso with(Context context) {
        com.squareup.picasso.Picasso picasso = com.squareup.picasso.Picasso.with(context);
        picasso.setIndicatorsEnabled(debug);
        picasso.setLoggingEnabled(debug);
        return picasso;
    }

    public static void loadImage(String url, ImageView imageView, @DrawableRes int fallbackDrawable) {
        if (url == null || url.isEmpty())
            loadLocalPlaceholders(imageView, fallbackDrawable);
        else
            Picasso.with(imageView)
                    .load(url)
                    .config(Bitmap.Config.RGB_565)
                    .into(imageView, new OnErrorCallback(() -> loadLocalPlaceholders(imageView,
                            fallbackDrawable)));
    }

    public static void tryToLoadImage(String url, ImageView imageView, @NonNull String fallbackUrl) {
        if (url == null || url.isEmpty())
            url = fallbackUrl;
        Picasso.with(imageView)
                .load(url)
                .config(Bitmap.Config.RGB_565)
                .into(imageView, new OnErrorCallback(() -> tryToLoadImage(fallbackUrl, imageView)));
    }

    public static void tryToLoadImage(String url, ImageView imageView) {
        if (url == null || url.isEmpty())
            return;
        Picasso.with(imageView)
                .load(url)
                .config(Bitmap.Config.RGB_565)
                .into(imageView);
    }

    public static void tryToLoadImage(String url, ImageView imageView, @DrawableRes int placeholder) {
        if (url == null || url.isEmpty())
            return;
        Picasso.with(imageView)
                .load(url)
                .config(Bitmap.Config.RGB_565)
                .error(placeholder)
                .placeholder(placeholder)
                .into(imageView);
    }

    public static void loadLocalPlaceholders(ImageView imageView, @DrawableRes int placeholder) {
        Picasso.with(imageView)
                .load(placeholder)
                .config(Bitmap.Config.RGB_565)
                .into(imageView);
    }

    public static void fetch(Context context, String s) {
        with(context).load(s).fetch();
    }

    /**
     * This will download the image synchronously, this should be done on a BackgroundThread
     *
     * @param context a context to get the Picasso
     * @param url     the wanted Url
     * @return true if got the image
     */
    @WorkerThread
    public static boolean fetchBlocking(Context context, @NonNull String url) {
        try {
            with(context).load(url).get();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static class OnErrorCallback implements Callback {

        private final Action0 onError;

        public OnErrorCallback(Action0 onError) {
            this.onError = onError;
        }

        @Override public void onSuccess() {

        }

        @Override public void onError() {
            onError.call();
        }
    }

    public static class OnSuccessTarget implements com.squareup.picasso.Target {

        @Override
        public void onBitmapLoaded(Bitmap bitmap, com.squareup.picasso.Picasso.LoadedFrom from) {

        }

        @Override public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    }

    public static class AnimatedTarget implements com.squareup.picasso.Target {

        private final ImageView view;
        private final int before;
        private final int after;

        public AnimatedTarget(@NonNull ImageView view, @AnimRes int animation) {
            this.view = view;
            this.after = animation;
            before = 0;
        }

        public AnimatedTarget(@NonNull ImageView view, @AnimRes int before, @AnimRes int after) {
            this.view = view;
            this.before = before;
            this.after = after;
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, com.squareup.picasso.Picasso.LoadedFrom from) {
            if (before > 0)
                runBefore(bitmap);
            else
                runAfter(bitmap);

        }

        private void runBefore(final Bitmap bitmap) {
            Animation animation = AnimationUtils.loadAnimation(view.getContext(), this.before);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    runAfter(bitmap);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            view.startAnimation(animation);
        }

        private void runAfter(Bitmap bitmap) {
            view.setImageBitmap(bitmap);
            Animation animation = AnimationUtils.loadAnimation(view.getContext(), this.after);
            view.startAnimation(animation);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    }
}
