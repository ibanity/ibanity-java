package com.ibanity.apis.client.products.isabel_connect.services;


import com.ibanity.apis.client.products.isabel_connect.models.Token;
import com.ibanity.apis.client.products.isabel_connect.models.create.TokenCreateQuery;
import com.ibanity.apis.client.products.isabel_connect.models.refresh.TokenRefreshQuery;
import com.ibanity.apis.client.products.isabel_connect.models.revoke.TokenRevokeQuery;

public interface TokenService {

    void revoke(TokenRevokeQuery transactionReadQuery);

    Token create(TokenCreateQuery tokenCreateQuery);

    Token refresh(TokenRefreshQuery tokenRefreshQuery);
}
