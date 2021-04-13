package com.ibanity.apis.client.http.service;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

public interface IbanityHttpSignatureService {

    /**
     * Alias to be used when the request has no payload.
     * @see IbanityHttpSignatureService#getHttpSignatureHeaders(String, URL, Map, String)
     * Allows you to create the needed headers to sign an http request following draft http signature
     * @see <a href="https://tools.ietf.org/html/draft-cavage-http-signatures-12">https://tools.ietf.org/html/draft-cavage-http-signatures-12</a>
     * @param httpMethod the http method of the current request.
     * @param url the url containing host, path and query parameters.
     * @param requestHeaders the headers of the current request. All ibanity-* headers will included in the signature.
     * @return the map with signature related headers: date, digest and signature headers.
     */
    Map<String, String> getHttpSignatureHeaders(String httpMethod, URL url, Map<String, String> requestHeaders);

    /**
     * Allows you to create the needed headers to sign an http request following draft http signature
     * @see <a href="https://tools.ietf.org/html/draft-cavage-http-signatures-12">https://tools.ietf.org/html/draft-cavage-http-signatures-12</a>
     * @param httpMethod the http method of the current request.
     * @param url the url containing host, path and query parameters.
     * @param requestHeaders the headers of the current request. All ibanity-* headers will included in the signature.
     * @param payload the payload of the actual request.
     * @return the map with signature related headers: date, digest and signature headers.
     */
    Map<String, String> getHttpSignatureHeaders(String httpMethod, URL url, Map<String, String> requestHeaders, String payload);

    /**
     * Allows you to create the needed headers to sign an http request following draft http signature
     * @see <a href="https://tools.ietf.org/html/draft-cavage-http-signatures-12">https://tools.ietf.org/html/draft-cavage-http-signatures-12</a>
     * @param httpMethod the http method of the current request.
     * @param url the url containing host, path and query parameters.
     * @param requestHeaders the headers of the current request. All ibanity-* headers will included in the signature.
     * @param payload the payload of the actual request as {@link java.io.InputStream}.
     * @return the map with signature related headers: date, digest and signature headers.
     */
    Map<String, String> getHttpSignatureHeaders(String httpMethod, URL url, Map<String, String> requestHeaders, InputStream payload);
}
