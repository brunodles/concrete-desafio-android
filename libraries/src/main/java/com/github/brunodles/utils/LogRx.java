package com.github.brunodles.utils;

import android.util.Log;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * This class is intended to be used with RX.
 * <p>
 * Created by brunodles on 16/10/15.
 */
public final class LogRx {

    private LogRx() {
    }

    public static Action1<Throwable> e(String tag) {
        return throwable -> Log.e(tag, "onError ", throwable);
    }

    public static Action1<Throwable> e(String tag, String msg) {
        return throwable -> Log.e(tag, msg, throwable);
    }

    public static <T> Action1<T> d(String tag, String msg) {
        return o -> Log.d(tag, msg);
    }

    public static <T> Action1<T> d(String tag, String format, Func1<T, Object>... formatArgs) {
        return o -> Log.d(tag, String.format(format,
                Observable.from(formatArgs)
                        .map(f -> f.call(o))
                        .toList()
                        .toBlocking()
                        .single()
                        .toArray()
        ));
    }
}