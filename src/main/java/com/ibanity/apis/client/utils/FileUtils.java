package com.ibanity.apis.client.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

public class FileUtils {
    public InputStream getInputStream(final String path) {
        try {
            File file = new File(path);
            if (file.isFile()) {
                return new FileInputStream(getFile(path));
            }
            return getClass().getClassLoader().getResourceAsStream(path);
        } catch (Exception e) {
            throw new IllegalArgumentException(new FileNotFoundException("Resource Path not found:" + path));
        }
    }

    public File getFile(final String path) {
        File file = new File(path);

        if (file.isFile()) {
            return file;
        }

        URL resourceURL = getClass().getClassLoader().getResource(path);
        if (resourceURL == null) {
            throw new IllegalArgumentException("Path " + path + " is invalid");
        }
        return new File(resourceURL.getFile());
    }

}
