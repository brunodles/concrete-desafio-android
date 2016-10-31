package com.github.brunodles.githubpopular.api.gson;

import com.github.brunodles.oleaster_suite_runner.OleasterSuiteRunner;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mscharhag.oleaster.matcher.matchers.ExceptionMatcher;

import org.junit.runner.RunWith;

import java.lang.reflect.Type;

import static com.mscharhag.oleaster.matcher.Matchers.expect;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.beforeEach;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.describe;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.it;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by bruno on 31/10/16.
 */
@RunWith(OleasterSuiteRunner.class)
public class BooleanDeserializerTest {

    private BooleanDeserializer deserializer;

    private Boolean result;
    private ExceptionMatcher.CodeBlock resultCodeBlock;

    {
        describe("Given a BooleanDeserializer", () -> {
            beforeEach(() -> deserializer = new BooleanDeserializer());

            whenDeserializeItShouldBe("0", false);
            whenDeserializeItShouldBe("1", true);
            whenDeserializeItShouldBe("f", false);
            whenDeserializeItShouldBe("t", true);
            whenDeserializeItShouldBe("F", false);
            whenDeserializeItShouldBe("T", true);
            whenDeserializeItShouldBe("true", true);
            whenDeserializeItShouldBe("false", false);

            whenDeserializeSholdThrow("unexpected", "unexpected");
            whenDeserializeSholdThrow("empty", "");
            whenDeserializeSholdThrow("null", null);

        });
    }

    private void whenDeserializeSholdThrow(String valueDescription, String value) {
        describe("When try to deserialize a " + valueDescription + " value", () -> {
            beforeEach(() -> {
                JsonElement json = mock(JsonElement.class);
                when(json.getAsString()).thenReturn(value);
                resultCodeBlock = () -> deserializer.deserialize(json, mock(Type.class), mock(JsonDeserializationContext.class));
            });
            it("should throw a JsonParseException", () -> {
                expect(resultCodeBlock).toThrow(JsonParseException.class);
            });
        });
    }

    private void whenDeserializeItShouldBe(String s, boolean expected) {
        describe("When to deserialize \"" + s + "\"", () -> {
            beforeEach(() -> {
                JsonElement json = mock(JsonElement.class);
                when(json.getAsString()).thenReturn(s);
                result = deserializer.deserialize(json, mock(Type.class), mock(JsonDeserializationContext.class));
            });
            it("should be " + expected, () -> expect(result).toEqual(expected));
        });
    }
}