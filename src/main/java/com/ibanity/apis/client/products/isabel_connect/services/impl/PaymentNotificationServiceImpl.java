package com.ibanity.apis.client.products.isabel_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.mappers.IsabelModelMapper;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.models.IsabelCollection;
import com.ibanity.apis.client.products.isabel_connect.models.PaymentNotification;
import com.ibanity.apis.client.products.isabel_connect.models.delete.PaymentNotificationDeleteQuery;
import com.ibanity.apis.client.products.isabel_connect.models.read.PaymentNotificationsReadQuery;
import com.ibanity.apis.client.products.isabel_connect.services.PaymentNotificationService;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;

import java.net.URI;

import static com.ibanity.apis.client.utils.URIHelper.buildUri;

public class PaymentNotificationServiceImpl implements PaymentNotificationService {
    private final ApiUrlProvider apiUrlProvider;
    private final IbanityHttpClient ibanityHttpClient;

    public PaymentNotificationServiceImpl(ApiUrlProvider apiUrlProvider, IbanityHttpClient ibanityHttpClient) {
        this.apiUrlProvider = apiUrlProvider;
        this.ibanityHttpClient = ibanityHttpClient;
    }

    @Override
    public IsabelCollection<PaymentNotification> list(PaymentNotificationsReadQuery query) {
        URI uri = buildUri(getNotificationsUrl(), query.getPagingSpec());
        HttpResponse response = ibanityHttpClient.get(uri, query.getAdditionalHeaders(), query.getAccessToken());
        return IsabelModelMapper.mapCollection(response, PaymentNotification.class);
    }

    @Override
    public void delete(PaymentNotificationDeleteQuery query) {
        URI uri = buildUri(getNotificationUrl(query.getPaymentNotificationId()));
        ibanityHttpClient.delete(uri, query.getAdditionalHeaders(), query.getAccessToken());
    }

    private String getNotificationsUrl() {
        String url = apiUrlProvider
                .find(IbanityProduct.IsabelConnect, "bulkPaymentInitiationRequests")
                .replace("{bulkPaymentInitiationRequestId}", "notifications");
        return StringUtils.removeEnd(url, "/");
    }

    private String getNotificationUrl(String paymentNotificationId) {
        String url = apiUrlProvider
                .find(IbanityProduct.IsabelConnect, "bulkPaymentInitiationRequests")
                .replace("{bulkPaymentInitiationRequestId}", "notifications/" + paymentNotificationId);
        return StringUtils.removeEnd(url, "/");
    }
}
