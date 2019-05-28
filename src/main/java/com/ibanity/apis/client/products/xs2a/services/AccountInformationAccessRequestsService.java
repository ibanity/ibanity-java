package com.ibanity.apis.client.products.xs2a.services;

import com.ibanity.apis.client.products.xs2a.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.products.xs2a.models.factory.create.AccountInformationAccessRequestCreationQuery;

public interface AccountInformationAccessRequestsService {

    AccountInformationAccessRequest create(
            AccountInformationAccessRequestCreationQuery accountInformationAccessRequestCreationQuery);

    AccountInformationAccessRequest find(
            AccountInformationAccessRequestCreationQuery accountInformationAccessRequestCreationQuery);

}
