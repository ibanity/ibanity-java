package com.ibanity.apis.client.products.isabel_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.products.isabel_connect.models.read.IsabelPagingSpec;
import com.ibanity.apis.client.products.isabel_connect.models.read.PaymentStatusReadQuery;
import com.ibanity.apis.client.products.isabel_connect.services.PaymentStatusService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static com.ibanity.apis.client.mappers.ModelMapperHelper.readResponseContent;
import static com.ibanity.apis.client.utils.URIHelper.buildUri;

public class PaymentStatusServiceImpl implements PaymentStatusService {
    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public PaymentStatusServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public String find(PaymentStatusReadQuery query) {
        Map<String, String> queryParams = new HashMap<>();
        if (StringUtils.isNotBlank(query.getNotificationId())) {
            queryParams.put("notificationId", query.getNotificationId());
        }
        IsabelPagingSpec emptyPagingSpec = IsabelPagingSpec.builder().size(null).build();
        URI uri = buildUri(getUrl(query.getBulkPaymentInitiationRequestId()), emptyPagingSpec, queryParams);
        HttpResponse response = ibanityHttpClient.get(uri, query.getAdditionalHeaders(), query.getAccessToken());
        try {
            return readResponseContent(response.getEntity());
        } catch (IOException exception) {
            throw new IllegalArgumentException("Response cannot be parsed", exception);
        }
    }

    private String getUrl(String bulkPaymentInitiationRequestId) {
        String url = apiUrlProvider
                .find(IbanityProduct.IsabelConnect, "bulkPaymentInitiationRequests")
                .replace("{bulkPaymentInitiationRequestId}", bulkPaymentInitiationRequestId);
        return StringUtils.removeEnd(url, "/") + "/payment-status";
    }
}
