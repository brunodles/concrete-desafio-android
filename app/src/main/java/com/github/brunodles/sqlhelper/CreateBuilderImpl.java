package com.github.brunodles.sqlhelper;

/**
 * Created by bruno on 19/10/16.
 */
class CreateBuilderImpl implements CreateBuilder {
    private StringBuilder str = new StringBuilder();

    public CreateBuilderImpl(String tableName) {
        str.append("CREATE TABLE " + tableName + "(");
    }

    @Override
    public CreateBuilder add(String fieldName, String type, String... modificators) {
        str.append(fieldName)
                .append(" ")
                .append(type);
        for (String modificator : modificators)
            str.append(" ").append(modificator);
        str.append(",");
        return this;
    }

    @Override
    public String build() {
        return str.deleteCharAt(str.length() - 1)
                .append(");")
                .toString();
    }
}
