package com.ibanity.apis.client.services;

import com.ibanity.apis.client.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.models.factory.create.AccountInformationAccessRequestCreationQuery;

public interface AccountInformationAccessRequestsService {

    AccountInformationAccessRequest create(
            AccountInformationAccessRequestCreationQuery accountInformationAccessRequestCreationQuery);

    AccountInformationAccessRequest find(
            AccountInformationAccessRequestCreationQuery accountInformationAccessRequestCreationQuery);

}
