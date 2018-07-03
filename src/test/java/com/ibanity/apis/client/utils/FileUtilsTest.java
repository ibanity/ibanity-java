package com.ibanity.apis.client.utils;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FileUtils Tester.
 *
 * @author Daniel De Luca
 * @version 1.0
 * @since <pre>Jun 13, 2018</pre>
 */
public class FileUtilsTest {

    FileUtils fileUtils = new FileUtils();

    @BeforeEach
    public void before() throws Exception {
    }

    @AfterEach
    public void after() throws Exception {
    }

    /**
     * Method: loadFile(String path)
     */
    @Test
    public void testLoadFile() throws Exception {
        InputStream inputStream = fileUtils.loadFile(getFilePath());
        assertNotNull(inputStream);
    }

    /**
     * Method: loadFile(String path)
     */
    @Test
    public void testLoadFileFailed() throws Exception {
        AtomicReference<InputStream> inputStream = new AtomicReference<>();
        assertThrows(IllegalArgumentException.class, () -> inputStream.set(fileUtils.loadFile(getFilePath() + "_NON_EXISTING")));
        assertNull(inputStream.get());
    }

    private String getFilePath() {
        String className = this.getClass().getName();
        return StringUtils.replace(className, ".", File.separator)+".class";
    }
} 
