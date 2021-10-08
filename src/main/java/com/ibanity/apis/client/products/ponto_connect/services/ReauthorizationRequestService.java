package com.ibanity.apis.client.products.ponto_connect.services;

import com.ibanity.apis.client.products.ponto_connect.models.ReauthorizationRequest;
import com.ibanity.apis.client.products.ponto_connect.models.create.ReauthorizationRequestCreateQuery;

public interface ReauthorizationRequestService {

    ReauthorizationRequest create(ReauthorizationRequestCreateQuery reauthorizationRequestCreateQuery);
}

