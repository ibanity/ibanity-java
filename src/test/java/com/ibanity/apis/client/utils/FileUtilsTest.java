package com.ibanity.apis.client.utils;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
public class FileUtilsTest {

    final FileUtils fileUtils = new FileUtils();

    @Test
    public void testLoadFile() {
        InputStream inputStream = fileUtils.getInputStream(getFilePath());
        assertNotNull(inputStream);
    }

    @Test
    public void testLoadFileFailed() {
        AtomicReference<InputStream> inputStream = new AtomicReference<>();
        assertThrows(IllegalArgumentException.class, () -> inputStream.set(fileUtils.getInputStream(getFilePath() + "_NON_EXISTING")));
        assertNull(inputStream.get());
    }

    private String getFilePath() {
        String className = this.getClass().getName();
        return StringUtils.replace(className, ".", File.separator)+".class";
    }
} 
