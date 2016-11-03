package com.github.brunodles.githubpopular.app.data;

import android.database.Cursor;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by bruno on 14/10/16.
 */

public final class Db {
    public static final String SEPARATOR = "__,__";
    public static final int BOOLEAN_FALSE = 0;
    public static final int BOOLEAN_TRUE = 1;

    private Db() {
    }

    public static String getString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndexOrThrow(columnName));
    }

    public static boolean getBoolean(Cursor cursor, String columnName) {
        return getInt(cursor, columnName) == BOOLEAN_TRUE;
    }

    public static long getLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndexOrThrow(columnName));
    }

    public static Float getFloat(Cursor cursor, String columnName) {
        return cursor.getFloat(cursor.getColumnIndexOrThrow(columnName));
    }

    public static int getInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndexOrThrow(columnName));
    }

    public static Integer getInteger(Cursor cursor, String columnName) {
        try {
            return cursor.getInt(cursor.getColumnIndexOrThrow(columnName));
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> String toList(Collection<T> items, Func1<T, Object> idMapper) {
        return Observable.from(items)
                .map(idMapper)
                .map(Object::toString)
                .reduce((s, s2) -> s + SEPARATOR + s2)
                .toBlocking().single();
    }

    public static <T> List<T> getList(Cursor cursor, String columnName, Func1<String, T> idMapper) {
        return Observable.just(getString(cursor, columnName))
                .map(s -> s.split(SEPARATOR))
                .flatMap(Observable::from)
                .map(idMapper)
                .toList()
                .toBlocking()
                .single();
    }

    @Nullable
    public static Long toLong(@Nullable Date date) {
        if (date == null) return null;
        return date.getTime();
    }

    @Nullable
    public static Date getDate(Cursor cursor, String columnName) {
        try {
            return new Date(getLong(cursor, columnName));
        } catch (Exception e) {
            return null;
        }
    }
}
