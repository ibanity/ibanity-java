package com.ibanity.apis.client.helpers;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

public class IbanityTestHelper {

    public static final ProtocolVersion HTTP = new ProtocolVersion("HTTP", 0, 0);

    public static HttpResponse loadHttpResponse(String filePath) throws IOException {
        String jsonResponse = loadFile(filePath);
        HttpResponse httpResponse = new BasicHttpResponse(HTTP, 200, null);
        httpResponse.setEntity(new StringEntity(jsonResponse));
        return httpResponse;
    }

    public static HttpResponse createHttpResponse(String expected) throws UnsupportedEncodingException {
        HttpResponse postResponse = new BasicHttpResponse(HTTP, 200, null);
        postResponse.setEntity(new StringEntity(expected));
        return postResponse;
    }

    private static String loadFile(String filePath) throws IOException {
        return IOUtils.toString(
                requireNonNull(IbanityTestHelper.class.getClassLoader().getResourceAsStream(filePath)), UTF_8);
    }
}
