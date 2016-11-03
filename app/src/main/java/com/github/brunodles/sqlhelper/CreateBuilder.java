package com.github.brunodles.sqlhelper;

/**
 * Created by bruno on 19/10/16.
 */

public interface CreateBuilder {
    public static final String INTEGER = "INTEGER";
    public static final String TEXT = "TEXT";
    public static final String REAL = "REAL";
    public static final String NOT_NULL = "NOT NULL";
    public static final String PK = "PRIMARY KEY";
    CreateBuilder add(String fieldName, String type, String... modificators);
    String build();
}
