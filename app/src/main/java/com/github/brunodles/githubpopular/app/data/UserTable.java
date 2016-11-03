package com.github.brunodles.githubpopular.app.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.github.brunodles.githubpopular.api.dto.User;
import com.github.brunodles.sqlhelper.SqlHelper;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

import static com.github.brunodles.sqlhelper.CreateBuilder.INTEGER;
import static com.github.brunodles.sqlhelper.CreateBuilder.PK;
import static com.github.brunodles.sqlhelper.CreateBuilder.TEXT;

public final class UserTable {

    public static final String TABLE_NAME = "USER";

    public static final String F_NAME = "name";
    public static final String F_COMPANY = "company";
    public static final String F_BLOG = "blog";
    public static final String F_LOCATION = "location";
    public static final String F_EMAIL = "email";
    public static final String F_HIREABLE = "hireable";
    public static final String F_BIO = "bio";
    public static final String F_PUBLIC_REPOS = "public_repos";
    public static final String F_PUBLIC_GISTS = "public_gists";
    public static final String F_FOLLOWERS = "followers";
    public static final String F_FOLLOWING = "following";
    public static final String F_CREATED_AT = "created_at";
    public static final String F_UPDATED_AT = "updated_at";
    public static final String F_LOGIN = "login";
    public static final String F_ID = "id";
    public static final String F_AVATAR_URL = "avatar_url";
    public static final String F_GRAVATAR_ID = "gravatar_id";
    public static final String F_URL = "url";
    public static final String F_HTML_URL = "html_url";
    public static final String F_FOLLOWERS_URL = "followers_url";
    public static final String F_FOLLOWING_URL = "following_url";
    public static final String F_GISTS_URL = "gists_url";
    public static final String F_STARRED_URL = "starred_url";
    public static final String F_SUBSCRIPTIONS_URL = "subscriptions_url";
    public static final String F_ORGANIZATIONS_URL = "organizations_url";
    public static final String F_REPOS_URL = "repos_url";
    public static final String F_EVENTS_URL = "events_url";
    public static final String F_RECEIVED_EVENTS_URL = "received_events_url";
    public static final String F_TYPE = "type";
    public static final String F_SITE_ADMIN = "site_admin";

    public static final String CREATE = SqlHelper.create(TABLE_NAME)
            .add(F_NAME, TEXT)
            .add(F_COMPANY, TEXT)
            .add(F_BLOG, TEXT)
            .add(F_LOCATION, TEXT)
            .add(F_EMAIL, TEXT)
            .add(F_HIREABLE, INTEGER)
            .add(F_BIO, TEXT)
            .add(F_PUBLIC_REPOS, INTEGER)
            .add(F_PUBLIC_GISTS, INTEGER)
            .add(F_FOLLOWERS, INTEGER)
            .add(F_FOLLOWING, INTEGER)
            .add(F_CREATED_AT, TEXT)
            .add(F_UPDATED_AT, TEXT)
            .add(F_LOGIN, TEXT, PK)
            .add(F_ID, INTEGER)
            .add(F_AVATAR_URL, TEXT)
            .add(F_GRAVATAR_ID, TEXT)
            .add(F_URL, TEXT)
            .add(F_HTML_URL, TEXT)
            .add(F_FOLLOWERS_URL, TEXT)
            .add(F_FOLLOWING_URL, TEXT)
            .add(F_GISTS_URL, TEXT)
            .add(F_STARRED_URL, TEXT)
            .add(F_SUBSCRIPTIONS_URL, TEXT)
            .add(F_ORGANIZATIONS_URL, TEXT)
            .add(F_REPOS_URL, TEXT)
            .add(F_EVENTS_URL, TEXT)
            .add(F_RECEIVED_EVENTS_URL, TEXT)
            .add(F_TYPE, TEXT)
            .add(F_SITE_ADMIN, INTEGER)
            .build();

    @NonNull
    private static ContentValues updateValues(User object) {
        ContentValues values = new ContentValues();
        values.put(F_NAME, object.name);
        values.put(F_COMPANY, object.company);
        values.put(F_BLOG, object.blog);
        values.put(F_LOCATION, object.location);
        values.put(F_EMAIL, object.email);
        values.put(F_HIREABLE, object.hireable);
        values.put(F_BIO, object.bio);
        values.put(F_PUBLIC_REPOS, object.public_repos);
        values.put(F_PUBLIC_GISTS, object.public_gists);
        values.put(F_FOLLOWERS, object.followers);
        values.put(F_FOLLOWING, object.following);
        values.put(F_CREATED_AT, object.created_at);
        values.put(F_UPDATED_AT, object.updated_at);
        values.put(F_LOGIN, object.login);
        values.put(F_ID, object.id);
        values.put(F_AVATAR_URL, object.avatar_url);
        values.put(F_GRAVATAR_ID, object.gravatar_id);
        values.put(F_URL, object.url);
        values.put(F_HTML_URL, object.html_url);
        values.put(F_FOLLOWERS_URL, object.followers_url);
        values.put(F_FOLLOWING_URL, object.following_url);
        values.put(F_GISTS_URL, object.gists_url);
        values.put(F_STARRED_URL, object.starred_url);
        values.put(F_SUBSCRIPTIONS_URL, object.subscriptions_url);
        values.put(F_ORGANIZATIONS_URL, object.organizations_url);
        values.put(F_REPOS_URL, object.repos_url);
        values.put(F_EVENTS_URL, object.events_url);
        values.put(F_RECEIVED_EVENTS_URL, object.received_events_url);
        values.put(F_TYPE, object.type);
        values.put(F_SITE_ADMIN, object.site_admin);
        return values;
    }

    public static final Func1<Cursor, User> MAPPER = (Func1<Cursor, User>) cursor -> {
        User result = new User();
        result.name = Db.getString(cursor, F_NAME);
        result.company = Db.getString(cursor, F_COMPANY);
        result.blog = Db.getString(cursor, F_BLOG);
        result.location = Db.getString(cursor, F_LOCATION);
        result.email = Db.getString(cursor, F_EMAIL);
        result.hireable = Db.getBoolean(cursor, F_HIREABLE);
        result.bio = Db.getString(cursor, F_BIO);
        result.public_repos = Db.getLong(cursor, F_PUBLIC_REPOS);
        result.public_gists = Db.getLong(cursor, F_PUBLIC_GISTS);
        result.followers = Db.getLong(cursor, F_FOLLOWERS);
        result.following = Db.getLong(cursor, F_FOLLOWING);
        result.created_at = Db.getString(cursor, F_CREATED_AT);
        result.updated_at = Db.getString(cursor, F_UPDATED_AT);
        result.login = Db.getString(cursor, F_LOGIN);
        result.id = Db.getLong(cursor, F_ID);
        result.avatar_url = Db.getString(cursor, F_AVATAR_URL);
        result.gravatar_id = Db.getString(cursor, F_GRAVATAR_ID);
        result.url = Db.getString(cursor, F_URL);
        result.html_url = Db.getString(cursor, F_HTML_URL);
        result.followers_url = Db.getString(cursor, F_FOLLOWERS_URL);
        result.following_url = Db.getString(cursor, F_FOLLOWING_URL);
        result.gists_url = Db.getString(cursor, F_GISTS_URL);
        result.starred_url = Db.getString(cursor, F_STARRED_URL);
        result.subscriptions_url = Db.getString(cursor, F_SUBSCRIPTIONS_URL);
        result.organizations_url = Db.getString(cursor, F_ORGANIZATIONS_URL);
        result.repos_url = Db.getString(cursor, F_REPOS_URL);
        result.events_url = Db.getString(cursor, F_EVENTS_URL);
        result.received_events_url = Db.getString(cursor, F_RECEIVED_EVENTS_URL);
        result.type = Db.getString(cursor, F_TYPE);
        result.site_admin = Db.getBoolean(cursor, F_SITE_ADMIN);
        return result;
    };

    public static Observable<List<User>> list(BriteDatabase db) {
        return db.createQuery(TABLE_NAME, "select * from " + TABLE_NAME).mapToList(MAPPER);
    }

    public static Observable<User> find(BriteDatabase db, String username) {
        return db.createQuery(TABLE_NAME, "select * from " + TABLE_NAME
                + " where " + F_LOGIN + " = \"" + username + "\"")
                .mapToOneOrDefault(MAPPER, null)
                .first();
//                .take(1)
//                .lift(SqlBrite.Query.mapToOneOrDefault(MAPPER, null));
    }

    public static long insert(BriteDatabase db, User object) {
        return db.insert(TABLE_NAME, updateValues(object), SQLiteDatabase.CONFLICT_REPLACE);
    }
}
