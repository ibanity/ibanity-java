package com.ibanity.apis.client.utils;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;

import static org.junit.Assert.assertNotNull;

/**
 * FileUtils Tester.
 *
 * @author Daniel De Luca
 * @version 1.0
 * @since <pre>Jun 13, 2018</pre>
 */
public class FileUtilsTest {

    FileUtils fileUtils = new FileUtils();

    @Before
    public void before() throws Exception {
    }

    @After
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
    @Test (expected = IllegalArgumentException.class)
    public void testLoadFileFailed() throws Exception {
        InputStream inputStream = fileUtils.loadFile(getFilePath()+"_NON_EXISTING");
        assertNotNull(inputStream);
    }

    private String getFilePath() {
        String className = this.getClass().getName();
        return StringUtils.replace(className, ".", File.separator)+".class";
    }
} 
