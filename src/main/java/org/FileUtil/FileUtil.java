package org.FileUtil;

import java.io.File;

public class FileUtil {
    public static boolean deleteAll(File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                return deleteAll(file);
            }
        }
        return file.delete();
    }
}
