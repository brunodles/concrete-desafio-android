package com.github.brunodles.githubpopular.api.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bruno on 07/10/15.
 */
public class BooleanDeserializer implements JsonDeserializer<Boolean> {

    private static final Map<String, Boolean> VALUE_MAP;

    static {
        HashMap<String, Boolean> values = new HashMap<>();
        values.put("0", false);
        values.put("1", true);
        values.put("false", false);
        values.put("true", true);
        values.put("f", false);
        values.put("t", true);
        VALUE_MAP = Collections.unmodifiableMap(values);
    }

    @Override
    public Boolean deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        String asString = json.getAsString().toLowerCase();
        Boolean result = VALUE_MAP.get(asString);
        if (result == null)
            throw new JsonParseException(String.format("\"%s\" is not a valid boolean", asString));
        return result;
    }
}
