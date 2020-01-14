package com.ibanity.apis.client.products.xs2a.services;

import com.ibanity.apis.client.products.xs2a.models.Authorization;
import com.ibanity.apis.client.products.xs2a.models.create.AccountInformationAccessRequestAuthorizationCreationQuery;
import com.ibanity.apis.client.products.xs2a.models.create.AuthorizationCreationQuery;

/**
 * @deprecated  Replaced by {@link com.ibanity.apis.client.products.xs2a.services.AccountInformationAccessRequestAuthorizationsService}
 */
@Deprecated
public interface AuthorizationsService {

    /**
     * @deprecated  Replaced by {@link com.ibanity.apis.client.products.xs2a.services.AccountInformationAccessRequestAuthorizationsService#create(AccountInformationAccessRequestAuthorizationCreationQuery)}
     */
    @Deprecated
    Authorization create(AuthorizationCreationQuery authorizationCreationQuery);
}
