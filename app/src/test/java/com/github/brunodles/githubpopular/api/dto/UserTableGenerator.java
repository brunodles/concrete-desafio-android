package com.github.brunodles.githubpopular.api.dto;

import com.github.brunodles.dbhelper.TableGeneratorReflection;

import java.util.List;

import static java.lang.String.format;

/**
 * Created by bruno on 02/11/16.
 */
public class UserTableGenerator {

    public static final String PUBLIC_FINAL_CLASS = "public final class %sTable {\n";
    public static final String FIELD_CONSTANT = "public static final String %s = \"%s\";\n";
    public static final String CREATE_TABLE_HEADER = "public static final String CREATE = SqlHelper.create(TABLE_NAME)\n";
    public static final String CONTENT_VALUES_DECLARATION = "@NonNull private static ContentValues updateValues(%s object) {\nContentValues values = new ContentValues();\n";
    public static final String MAPPER_DECLARATION = "public static final Func1<Cursor, %1$s> MAPPER = (Func1<Cursor, %1$s>) cursor -> {\n";
    public static final String LIST_METHOD = "public static Observable<List<%s>> list(BriteDatabase db) {\nreturn db.createQuery(TABLE_NAME, \"select * from \" + TABLE_NAME).mapToList(MAPPER);\n}";
    public static final String INSERT_OBJECT = "public static long insert(BriteDatabase db, %s object) {\nreturn db.insert(TABLE_NAME, updateValues(object), SQLiteDatabase.CONFLICT_REPLACE);\n}";

    public static void main(String[] args) {
        TableGeneratorReflection table = new TableGeneratorReflection(User.class);
        List<TableGeneratorReflection.Field> fields = table.getFields();

        StringBuilder mainBuilder = new StringBuilder();

        mainBuilder.append(classHeader(table))
                .append(tableNameConstant(table));

        StringBuilder createTableBuilder = new StringBuilder(CREATE_TABLE_HEADER);

        StringBuilder contentValuesBuilder = new StringBuilder()
                .append(format(CONTENT_VALUES_DECLARATION, table.typeClassName()));

        StringBuilder mapperBuilder = new StringBuilder()
                .append(format(MAPPER_DECLARATION, table.typeClassName()))
                .append(format("%s result = new %1$s();\n", table.typeClassName()));

        for (TableGeneratorReflection.Field field : fields) {
            mainBuilder.append(constant(fieldName(field), field.name.toLowerCase()));
            createTableBuilder.append(createTableField(field));
            contentValuesBuilder.append(format("values.put(%s, object.%s);\n", fieldName(field), field.name));

            mapperBuilder.append(format("result.%s = %s;\n", field.name, readFieldFromDb(field)));
        }

        createTableBuilder.append(".build();");
        contentValuesBuilder.append("return values;\n}");
        mapperBuilder.append("return result;\n};");


        mainBuilder.append("\n\n")
                .append(createTableBuilder)
                .append("\n\n")
                .append(contentValuesBuilder)
                .append("\n\n")
                .append(mapperBuilder)
                .append("\n\n")
                .append(insertObject(table))
                .append(listObjects(table))
                .append("}");

        System.out.println(mainBuilder);
    }

    private static String insertObject(TableGeneratorReflection table) {
        return format(INSERT_OBJECT, table.typeClassName());
    }

    private static String listObjects(TableGeneratorReflection table) {
        return format(LIST_METHOD, table.typeClassName());
    }

    private static String readFieldFromDb(TableGeneratorReflection.Field field) {
        return format("Db.get%s(cursor, %s)", capitalFirst(field.type), fieldName(field));
    }

    private static String createTableField(TableGeneratorReflection.Field field) {
        StringBuilder modifiers = new StringBuilder();
        for (String s : field.getModifiers())
            modifiers.append(", ")
                    .append(s);
        try {
            return format(".add(%s, %s %s)\n", fieldName(field), field.getDbFieldType(), modifiers);
        } catch (Exception e) {
            return format("//.add(%s, %s %s)\n", fieldName(field), field.type, modifiers);
        }
    }

    private static String classHeader(TableGeneratorReflection table) {
        return format(PUBLIC_FINAL_CLASS, table.typeClassName());
    }

    private static String tableNameConstant(TableGeneratorReflection table) {
        return constant("TABLE_NAME", table.getTableName());
    }

    private static String constant(String name, String value) {
        return format(FIELD_CONSTANT, name, value);
    }

    private static String capitalFirst(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static final String fieldName(TableGeneratorReflection.Field field) {
        return "F_" + field.name.toUpperCase();
    }
}
