package com.ibanity.apis.client.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileUtils {
    public InputStream loadFile(String path) {
        File file  = new File(path);
        if (file.isFile()){
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                return getInputStreamFromResources(path);
            }
        } else {
            return getInputStreamFromResources(path);
        }
    }

    private InputStream getInputStreamFromResources(String path){
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = null;
        try {
            File file = new File(classLoader.getResource(path).getFile());
            inputStream = new FileInputStream(file);
        } catch (Exception e) {
            throw new IllegalArgumentException("Resource Path not found:"+path+":", e);
        }
        return inputStream;
    }
}
