package com.ibanity.apis.client.services;

import com.ibanity.apis.client.models.AccountInformationAccessRequest;

import java.util.UUID;

public interface AccountInformationAccessRequestsService {

    AccountInformationAccessRequest create(String customerAccessToken, UUID financialInstitutionId,
                                           String redirectURI, String consentReference);

    AccountInformationAccessRequest create(String customerAccessToken, UUID financialInstitutionId,
                                           String redirectURI, String consentReference, UUID idempotencyKey);

}
