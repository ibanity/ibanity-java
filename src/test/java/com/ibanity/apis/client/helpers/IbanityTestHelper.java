package com.ibanity.apis.client.helpers;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.Charset;

import static java.util.Objects.requireNonNull;

public class IbanityTestHelper {

    private static final Charset UTF8_ENCODING = Charset.forName("UTF-8");

    public static String loadFile(String filePath) throws IOException {
        String s = IOUtils.toString(
                requireNonNull(ClassLoader.getSystemClassLoader().getResourceAsStream(filePath)), UTF8_ENCODING
        );
        System.out.println(s);
        return s;
    }
}