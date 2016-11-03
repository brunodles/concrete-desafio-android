package com.github.brunodles.dbhelper;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

/**
 * Created by bruno de lima on 20/04/14.
 */

public class TableGeneratorReflection {

    public static final List<String> INVALID_MODIFICATORS = unmodifiableList(asList(
            "static", "final", "abstract"));
    private Class aClass;
    private List<Field> fields;

    public TableGeneratorReflection(Class aClass) {
        super();
        this.aClass = aClass;
        fields = findFields(aClass);
    }

    private static List<Field> findFields(Class aClass) {
        List<Field> fields = new ArrayList<>();
        for (java.lang.reflect.Field field : aClass.getDeclaredFields()) {
            Field newField = buildField(field);
            fields.add(newField);
        }
        Class superclass = aClass.getSuperclass();
        if (superclass != Object.class)
            fields.addAll(findFields(superclass));
        return fields;
    }

    private static Field buildField(java.lang.reflect.Field field) {
        Field newField = new Field(field.getName(), field.getType().getSimpleName());
        Annotation[] declaredAnnotations = field.getDeclaredAnnotations();
        for (Annotation declaredAnnotation : declaredAnnotations) {
            if (declaredAnnotation.getClass().getName().contains("NonNull"))
                newField.addModifier("NonNull");
        }
        int modifiers = field.getModifiers();
        // TODO verify if is a valid modifiers
        return newField;
    }

    public List<String> getFieldsNames() {
        ArrayList<String> result = new ArrayList<String>();
        for (Field field : fields) {
            result.add(field.name);
        }
        return result;
    }

    public String getTableName() {
        return Utils.classNameToTableName(aClass.getSimpleName());
    }

    public List<Field> getFields() {
        return unmodifiableList(fields);
    }

    public Field getField(int index) {
        return fields.get(index);
    }

    public String typeClassName() {
        return aClass.getSimpleName();
    }

    public static class Field {

        private static final List<String> INTEGER_TYPES = asList("int", "integer", "long", "bigint", "boolean", "date");
        private static final List<String> REAL_TYPES = asList("float", "double");
        private static final List<String> TEXT_TYPES = asList("string", "list");

        public final String name;
        public final String type;
        private final List<String> modifiers = new ArrayList<>();

        public Field(String name, String type) {
            this.name = name;
            this.type = type;
        }

        public String getDbFieldType() {
            final String typeLowerCase = type.toLowerCase();
            if (INTEGER_TYPES.contains(typeLowerCase)) {
                return "INTEGER";
            } else if (REAL_TYPES.contains(typeLowerCase)) {
                return "REAL";
            } else if (TEXT_TYPES.contains(typeLowerCase)) {
                return "TEXT";
            }

            throw new RuntimeException("Unmapped field type for \"" + typeLowerCase + "\"");
        }

        public List<String> getModifiers() {
            return unmodifiableList(modifiers);
        }

        void addModifier(String name) {
            modifiers.add(name);
        }
    }

    public static class Utils {

        public static final String CAMEL_CASE_REGEX = "(\\p{Upper}*)(\\p{Lower}+)";

        public static String classNameToTableName(String className) {
            Matcher matcher = Pattern.compile(CAMEL_CASE_REGEX).matcher(className);
            String name = "";
            while (matcher.find()) {
                String uppercase = matcher.group(1);
                String lowercase = matcher.group(2);
                if (uppercase != null && !uppercase.isEmpty()) {
                    name += uppercase;
                }
                name += lowercase + "_";
            }
            return name.substring(0, name.length() - 1).toUpperCase();
        }
    }
}
