package com.github.brunodles.database.sqlhelper;

/**
 * Created by bruno on 19/10/16.
 */

public final class SqlHelper {
    private SqlHelper() {
    }

    public static CreateBuilder create(String tableName) {
        return new CreateBuilderImpl(tableName);
    }
}
