package com.ibanity.apis.client.products.oauth2.services;


import com.ibanity.apis.client.products.oauth2.models.Token;
import com.ibanity.apis.client.products.oauth2.models.create.TokenCreateQuery;
import com.ibanity.apis.client.products.oauth2.models.refresh.TokenRefreshQuery;
import com.ibanity.apis.client.products.oauth2.models.revoke.TokenRevokeQuery;

public interface TokenService {

    void revoke(TokenRevokeQuery transactionReadQuery);

    Token create(TokenCreateQuery tokenCreateQuery);

    Token refresh(TokenRefreshQuery tokenRefreshQuery);
}
