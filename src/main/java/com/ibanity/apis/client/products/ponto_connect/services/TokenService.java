package com.ibanity.apis.client.products.ponto_connect.services;

import com.ibanity.apis.client.products.ponto_connect.models.Token;
import com.ibanity.apis.client.products.ponto_connect.models.create.TokenCreateQuery;
import com.ibanity.apis.client.products.ponto_connect.models.refresh.TokenRefreshQuery;
import com.ibanity.apis.client.products.ponto_connect.models.revoke.TokenRevokeQuery;

public interface TokenService {

    void revoke(TokenRevokeQuery transactionReadQuery);

    Token create(TokenCreateQuery tokenCreateQuery);

    Token refresh(TokenRefreshQuery tokenRefreshQuery);
}
