package com.ibanity.apis.client.products.xs2a.services;

import com.ibanity.apis.client.products.xs2a.models.AccountInformationAccessRequestAuthorization;
import com.ibanity.apis.client.products.xs2a.models.create.AccountInformationAccessRequestAuthorizationCreationQuery;

public interface AccountInformationAccessRequestAuthorizationsService {

    AccountInformationAccessRequestAuthorization create(AccountInformationAccessRequestAuthorizationCreationQuery authorizationCreationQuery);
}
