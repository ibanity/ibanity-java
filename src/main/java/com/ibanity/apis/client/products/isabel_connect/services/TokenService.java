package com.ibanity.apis.client.products.isabel_connect.services;


import com.ibanity.apis.client.products.isabel_connect.models.AccessToken;
import com.ibanity.apis.client.products.isabel_connect.models.InitialToken;
import com.ibanity.apis.client.products.isabel_connect.models.create.AccessTokenCreateQuery;
import com.ibanity.apis.client.products.isabel_connect.models.create.InitialTokenCreateQuery;
import com.ibanity.apis.client.products.isabel_connect.models.revoke.TokenRevokeQuery;

public interface TokenService {

    void revoke(TokenRevokeQuery transactionReadQuery);

    InitialToken create(InitialTokenCreateQuery initialTokenCreateQuery);

    AccessToken create(AccessTokenCreateQuery accessTokenCreateQuery);
}
