package com.github.brunodles.githubpopular.app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bruno on 14/10/16.
 */

public class AppDataOpenHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;

    public AppDataOpenHelper(Context context) {
        super(context, "githubpop.db", null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserTable.CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
