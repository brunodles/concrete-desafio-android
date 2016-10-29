package com.github.brunodles;

import java.io.File;

/**
 * Created by bruno on 29/10/16.
 */

public class FileHelper {
    public static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }
}
