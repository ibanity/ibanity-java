package com.ibanity.apis.client.services;

import com.ibanity.apis.client.models.AccountInformationAccessRequest;

import java.util.UUID;

public interface AccountInformationAccessRequestsService {

    AccountInformationAccessRequest createForFinancialInstitution(String customerAccessToken, UUID financialInstitutionId, String redirectURI, String consentReference);

    AccountInformationAccessRequest createForFinancialInstitution(String customerAccessToken, UUID financialInstitutionId, String redirectURI, String consentReference, UUID idempotencyKey);

}
