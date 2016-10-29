package com.github.brunodles.testhelper;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

/**
 * Created by bruno on 29/10/16.
 */

public class TestResourceHelper {

    private static final String TEST_RESOURCES = "src/test/resources/";


    public static String resourceFile(String name) throws FileNotFoundException {
        return readFile(new File(TEST_RESOURCES, name));
    }

    public static String resourceFile(Class aClass, String name) throws FileNotFoundException {
        return readFile(new File(TEST_RESOURCES + packageToPath(aClass), name));
    }

    @Deprecated()
    public static String resouce(String name) throws FileNotFoundException {
        URL url = TestResourceHelper.class.getClassLoader().getResource(name);
        File file = new File(url.getFile());
        return readFile(file);
    }

    @Deprecated()
    public static String resouce(Class aClass, String name) throws FileNotFoundException {
        URL url = aClass.getResource(name);
        File file = new File(url.getFile());
        return readFile(file);
    }

    private static String packageToPath(Class aClass) {
        return aClass.getPackage().getName().replace(".", "/") + "/";
    }

    private static String readFile(File file) throws FileNotFoundException {
        return new java.util.Scanner(file).useDelimiter("\\Z").next();
    }
}
