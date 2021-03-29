package com.ibanity.apis.client.products.isabel_connect.services.impl;

import com.ibanity.apis.client.http.handler.IbanityResponseHandler;
import com.ibanity.apis.client.mappers.IsabelModelMapper;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.isabel_connect.models.BulkPaymentInitiationRequest;
import com.ibanity.apis.client.products.isabel_connect.models.create.BulkPaymentInitiationRequestCreateQuery;
import com.ibanity.apis.client.products.isabel_connect.services.BulkPaymentInitiationRequestService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import com.ibanity.apis.client.utils.URIHelper;
import lombok.NonNull;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.FileEntity;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import static org.apache.http.HttpHeaders.AUTHORIZATION;

public class BulkPaymentInitiationRequestServiceImpl implements BulkPaymentInitiationRequestService {
    private final ApiUrlProvider apiUrlProvider;
    private final IbanityResponseHandler ibanityResponseHandler;
    private final HttpClient httpClient;

    public BulkPaymentInitiationRequestServiceImpl(
            ApiUrlProvider apiUrlProvider,
            HttpClient httpClient,
            IbanityResponseHandler ibanityResponseHandler) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityResponseHandler = ibanityResponseHandler;
        this.httpClient = httpClient;
    }

    @Override
    public BulkPaymentInitiationRequest create(BulkPaymentInitiationRequestCreateQuery query) {
        URI url = URIHelper.buildUri(getUrl());
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Accept", "application/vnd.api+json");
        httpPost.setHeader("Content-type", "application/xml");
        FileEntity entity = new FileEntity(query.getFile());
        entity.setChunked(true);
        httpPost.setEntity(entity);

        HttpResponse res = execute(query.getAdditionalHeaders(), query.getAccessToken(), httpPost);

        return IsabelModelMapper.mapResource(res, BulkPaymentInitiationRequest.class);
    }

    private String getUrl() {
        return getUrl("");
    }

    private String getUrl(String bulkPaymentInitiationRequestId) {
        String url = apiUrlProvider
                .find(IbanityProduct.IsabelConnect, "bulk-payment-initiation-request")
                .replace("{bulkPaymentInitiationRequestId}", bulkPaymentInitiationRequestId);

        return StringUtils.removeEnd(url, "/");
    }

    private HttpResponse execute(@NonNull Map<String, String> additionalHeaders,
                                 String customerAccessToken,
                                 HttpRequestBase httpRequestBase) {
        try {
            addHeaders(httpRequestBase, customerAccessToken, additionalHeaders);
            return ibanityResponseHandler.handleResponse(this.httpClient.execute(httpRequestBase));
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
