package com.ibanity.apis.client.services;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.products.isabel_connect.services.IsabelConnectService;
import com.ibanity.apis.client.products.ponto_connect.services.PontoConnectService;
import com.ibanity.apis.client.products.xs2a.services.Xs2aService;
import com.ibanity.apis.client.webhooks.services.WebhooksService;

public interface IbanityService {
    ApiUrlProvider apiUrlProvider();

    IbanityHttpClient ibanityHttpClient();

    Xs2aService xs2aService();

    PontoConnectService pontoConnectService();

    IsabelConnectService isabelConnectService();

    WebhooksService webhooksService();
}
