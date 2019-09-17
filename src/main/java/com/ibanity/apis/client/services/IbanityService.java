package com.ibanity.apis.client.services;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.products.ponto_connect.services.PontoConnectService;
import com.ibanity.apis.client.products.xs2a.services.Xs2aService;

public interface IbanityService {
    ApiUrlProvider apiUrlProvider();

    IbanityHttpClient ibanityHttpClient();

    Xs2aService xs2aService();

    PontoConnectService pontoConnectService();
}
