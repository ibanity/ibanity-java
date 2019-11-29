package com.ibanity.apis.client.products.xs2a.services;

import com.ibanity.apis.client.products.xs2a.models.Authorization;
import com.ibanity.apis.client.products.xs2a.models.create.AuthorizationCreationQuery;

public interface AuthorizationsService {

    Authorization create(AuthorizationCreationQuery authorizationCreationQuery);
}
