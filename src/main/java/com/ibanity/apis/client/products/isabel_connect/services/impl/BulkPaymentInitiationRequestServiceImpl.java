package com.ibanity.apis.client.products.isabel_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.http.handler.IbanityResponseHandler;
import com.ibanity.apis.client.mappers.IsabelModelMapper;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.isabel_connect.models.BulkPaymentInitiationRequest;
import com.ibanity.apis.client.products.isabel_connect.models.create.BulkPaymentInitiationRequestCreateQuery;
import com.ibanity.apis.client.products.isabel_connect.models.read.BulkPaymentInitiationRequestReadQuery;
import com.ibanity.apis.client.products.isabel_connect.services.BulkPaymentInitiationRequestService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Map;

import static com.ibanity.apis.client.utils.URIHelper.buildUri;
import static org.apache.http.HttpHeaders.AUTHORIZATION;

public class BulkPaymentInitiationRequestServiceImpl implements BulkPaymentInitiationRequestService {
    private final ApiUrlProvider apiUrlProvider;
    private final IbanityResponseHandler ibanityResponseHandler;
    private final IbanityHttpClient ibanityHttpClient;

    public BulkPaymentInitiationRequestServiceImpl(
            ApiUrlProvider apiUrlProvider,
            IbanityResponseHandler ibanityResponseHandler,
            IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityResponseHandler = ibanityResponseHandler;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    @SneakyThrows(UnsupportedEncodingException.class)
    public BulkPaymentInitiationRequest create(BulkPaymentInitiationRequestCreateQuery query) {
        URI url = buildUri(getUrl());
        HttpPost httpPost = new HttpPost(url);
        setHeaders(httpPost, query);

        AbstractHttpEntity entity =
                query.getFile() != null ?
                        new FileEntity(query.getFile()) :
                        new StringEntity(query.getContent());

        entity.setChunked(true);
        httpPost.setEntity(entity);

        HttpResponse res = execute(query.getAdditionalHeaders(), query.getAccessToken(), httpPost);

        return IsabelModelMapper.mapResource(res, BulkPaymentInitiationRequest.class);
    }

    private void setHeaders(HttpPost httpPost, BulkPaymentInitiationRequestCreateQuery query) {
        httpPost.setHeader("Accept", "application/vnd.api+json");
        httpPost.setHeader("Content-Type", "application/xml");
        httpPost.setHeader("Content-Disposition", "inline; filename=" + query.getFilename());

        if (query.getShared() != null) {
            httpPost.setHeader("Is-Shared", query.getShared().toString());
        }

        if (query.getHideDetails() != null ) {
            httpPost.setHeader("Hide-Details", query.getHideDetails().toString());
        }
    }

    @Override
    public BulkPaymentInitiationRequest find(BulkPaymentInitiationRequestReadQuery query) {
        URI uri = buildUri(getUrl(query.getBulkPaymentInitiationRequestId()));
        HttpResponse response = ibanityHttpClient.get(uri, query.getAdditionalHeaders(), query.getAccessToken());

        return IsabelModelMapper.mapResource(response, BulkPaymentInitiationRequest.class);
    }

    private String getUrl() {
        return getUrl("");
    }

    private String getUrl(String bulkPaymentInitiationRequestId) {
        String url = apiUrlProvider
                .find(IbanityProduct.IsabelConnect, "bulkPaymentInitiationRequests")
                .replace("{bulkPaymentInitiationRequestId}", bulkPaymentInitiationRequestId);

        return StringUtils.removeEnd(url, "/");
    }

    private HttpResponse execute(@NonNull Map<String, String> additionalHeaders,
                                 String customerAccessToken,
                                 HttpRequestBase httpRequestBase) {
        try {
            addHeaders(httpRequestBase, customerAccessToken, additionalHeaders);
            return ibanityResponseHandler.handleResponse(this.ibanityHttpClient.httpClient().execute(httpRequestBase));
        } catch (IOException exception) {
            throw new RuntimeException("An error occurred while connecting to Ibanity", exception);
        }
    }

    private void addHeaders(HttpRequestBase httpRequestBase,
                            String customerAccessToken,
                            Map<String, String> additionalHeaders) {
        addAuthorizationHeader(httpRequestBase, customerAccessToken);
        additionalHeaders.forEach(httpRequestBase::addHeader);
    }

    private void addAuthorizationHeader(HttpRequestBase requestBase, String customerAccessToken) {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(customerAccessToken)) {
            requestBase.addHeader(new BasicHeader(AUTHORIZATION, "Bearer " + customerAccessToken));
        }
    }
}
