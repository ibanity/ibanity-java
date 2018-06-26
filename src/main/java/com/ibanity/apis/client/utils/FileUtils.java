package com.ibanity.apis.client.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class FileUtils {
    public InputStream loadFile(final String path) {
        try {
            return new FileInputStream(getFile(path));
        } catch (Exception e) {
            throw new IllegalArgumentException("Resource Path not found:" + path + ":", e);
        }
    }

    public File getFile(final String path) {
        File file  = new File(path);
        if (file.isFile()) {
            return file;
        } else {
            ClassLoader classLoader = getClass().getClassLoader();
            file = new File(classLoader.getResource(path).getFile());
        }
        return file;
    }
}
