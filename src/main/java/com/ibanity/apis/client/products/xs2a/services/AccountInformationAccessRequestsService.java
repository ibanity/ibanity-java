package com.ibanity.apis.client.products.xs2a.services;

import com.ibanity.apis.client.products.xs2a.models.AccountInformationAccessRequest;
import com.ibanity.apis.client.products.xs2a.models.create.AccountInformationAccessRequestCreationQuery;
import com.ibanity.apis.client.products.xs2a.models.read.AccountInformationAccessRequestReadQuery;

public interface AccountInformationAccessRequestsService {

    AccountInformationAccessRequest create(
            AccountInformationAccessRequestCreationQuery accountInformationAccessRequestCreationQuery);

    /**
     * This method uses an ambiguous parameter.
     *
     * @deprecated use {@link #find(AccountInformationAccessRequestReadQuery)} instead.
     */
    @Deprecated
    AccountInformationAccessRequest find(
            AccountInformationAccessRequestCreationQuery accountInformationAccessRequestCreationQuery);

    AccountInformationAccessRequest find(
            AccountInformationAccessRequestReadQuery accountInformationAccessRequestReadQuery);

}
