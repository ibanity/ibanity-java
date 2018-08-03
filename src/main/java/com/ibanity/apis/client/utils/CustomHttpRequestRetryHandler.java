package com.ibanity.apis.client.utils;

/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */


import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.RequestWrapper;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * The custom {@link HttpRequestRetryHandler} used by request executors.
 *
 * @since 4.0
 */
public class CustomHttpRequestRetryHandler implements HttpRequestRetryHandler {
    private static final Logger LOGGER = LogManager.getLogger(CustomHttpRequestRetryHandler.class);
    private static final int DEFAULT_RETRY_COUNT = 3;

    /** the number of times a method will be retried */
    private final int retryCount;

    /** Whether or not methods that have successfully sent their request will be retried */
    private final boolean requestSentRetryEnabled;

    private final Set<Class<? extends IOException>> nonRetriableClasses;

    /**
     * Create the request retry handler using the specified IOException classes
     *
     * @param retryCount how many times to retry; 0 means no retries
     * @param requestSentRetryEnabled true if it's OK to retry requests that have been sent
     * @param clazzes the IOException types that should not be retried
     * @since 4.3
     */
    protected CustomHttpRequestRetryHandler(
            final int retryCount,
            final boolean requestSentRetryEnabled,
            final Collection<Class<? extends IOException>> clazzes) {
        super();
        this.retryCount = retryCount;
        this.requestSentRetryEnabled = requestSentRetryEnabled;
        this.nonRetriableClasses = new HashSet<>();
        this.nonRetriableClasses.addAll(clazzes);
    }

    /**
     * Create the request retry handler using the following list of
     * non-retriable IOException classes: <br>
     * <ul>
     * <li>InterruptedIOException</li>
     * <li>UnknownHostException</li>
     * <li>ConnectException</li>
     * <li>SSLException</li>
     * </ul>
     * @param retryCount how many times to retry; 0 means no retries
     * @param requestSentRetryEnabled true if it's OK to retry non-idempotent requests that have been sent
     */
    @SuppressWarnings("unchecked")
    public CustomHttpRequestRetryHandler(final int retryCount, final boolean requestSentRetryEnabled) {
        this(retryCount, requestSentRetryEnabled, Arrays.asList(
                InterruptedIOException.class,
                UnknownHostException.class,
                ConnectException.class,
                SSLException.class));
    }

    /**
     * Create the request retry handler with a retry count of 3, requestSentRetryEnabled false
     * and using the following list of non-retriable IOException classes: <br>
     * <ul>
     * <li>InterruptedIOException</li>
     * <li>UnknownHostException</li>
     * <li>ConnectException</li>
     * <li>SSLException</li>
     * </ul>
     */
    public CustomHttpRequestRetryHandler() {
        this(DEFAULT_RETRY_COUNT, false);
    }
    /**
     * Used {@code retryCount} and {@code requestSentRetryEnabled} to determine
     * if the given method should be retried.
     */
    @Override
    public boolean retryRequest(
            final IOException exception,
            final int executionCount,
            final HttpContext context) {
        Args.notNull(exception, "Exception parameter");
        Args.notNull(context, "HTTP context");

        LOGGER.debug("Retry request");

        if (executionCount > this.retryCount) {
            // Do not retry if over max retry count
            LOGGER.debug("Do not retry HttpRequest anymore because over max retryCount");
            return false;
        }
        if (this.nonRetriableClasses.contains(exception.getClass())) {
            if (exception.getClass().equals(SSLException.class)) {
                SSLException sslException = (SSLException) exception;
                if ("Received fatal alert: unexpected_message".equals(sslException.getMessage())) {
                    LOGGER.debug("Retry on unexpected_message");
                    return true;
                }
                LOGGER.debug("Don't retry because SSLException with message " + sslException.getMessage());
                return false;
            }
            LOGGER.debug("Do not retry HttpRequest anymore because non retriable exception class " + exception.getClass());
            return false;
        } else {
            for (final Class<? extends IOException> rejectException : this.nonRetriableClasses) {
                if (rejectException.isInstance(exception)) {
                    LOGGER.debug("Do not retry HttpRequest anymore because instance of non-retriable exception class " + exception.getClass());
                    return false;
                }
            }
        }
        final HttpClientContext clientContext = HttpClientContext.adapt(context);
        final HttpRequest request = clientContext.getRequest();

        if (requestIsAborted(request)) {
            LOGGER.debug("Do not retry HttpRequest anymore because request is aborted");
            return false;
        }

        if (handleAsIdempotent(request)) {
            // Retry if the request is considered idempotent
            LOGGER.debug("Retry because request is idempotent!");
            return true;
        }

        if (!clientContext.isRequestSent() || this.requestSentRetryEnabled) {
            // Retry if the request has not been sent fully or
            // if it's OK to retry methods that have been sent
            LOGGER.debug("Retry because request not fully sent");
            return true;
        }
        // otherwise do not retry
        LOGGER.debug("Do not retry HttpRequest anymore because ... no other conditions");
        return false;
    }

    /**
     * @return {@code true} if this handler will retry methods that have
     * successfully sent their request, {@code false} otherwise
     */
    public boolean isRequestSentRetryEnabled() {
        return requestSentRetryEnabled;
    }

    /**
     * @return the maximum number of times a method will be retried
     */
    public int getRetryCount() {
        return retryCount;
    }

    /**
     * @since 4.2
     *
     * @param request the request to verify
     */
    protected boolean handleAsIdempotent(final HttpRequest request) {
        return !(request instanceof HttpEntityEnclosingRequest);
    }

    /**
     * @since 4.2
     *
     * @deprecated (4.3)
     *
     * @param request the request to check if aborted
     */
    @Deprecated
    protected boolean requestIsAborted(final HttpRequest request) {
        HttpRequest req = request;
        if (request instanceof RequestWrapper) { // does not forward request to original
            req = ((RequestWrapper) request).getOriginal();
        }
        return (req instanceof HttpUriRequest && ((HttpUriRequest) req).isAborted());
    }

}
