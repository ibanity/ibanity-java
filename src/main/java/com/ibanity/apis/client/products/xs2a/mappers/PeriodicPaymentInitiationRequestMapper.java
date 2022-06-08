package com.ibanity.apis.client.products.xs2a.mappers;

import com.ibanity.apis.client.jsonapi.DataApiModel;
import com.ibanity.apis.client.jsonapi.RelationshipsApiModel;
import com.ibanity.apis.client.mappers.IbanityModelMapper;
import com.ibanity.apis.client.products.xs2a.models.create.PeriodicPaymentInitiationRequestCreationQuery;
import com.ibanity.apis.client.products.xs2a.models.links.FinancialInstitutionLinks;
import com.ibanity.apis.client.products.xs2a.models.links.PeriodicPaymentInitiationAuthorizationLinks;
import com.ibanity.apis.client.products.xs2a.services.impl.PeriodicPaymentInitiationRequestServiceImpl;

import java.util.UUID;
import java.util.function.Function;

public class PeriodicPaymentInitiationRequestMapper {

    public static PeriodicPaymentInitiationRequestServiceImpl.PeriodicPaymentInitiationRequest getRequestMapping(PeriodicPaymentInitiationRequestCreationQuery query) {
        return PeriodicPaymentInitiationRequestServiceImpl.PeriodicPaymentInitiationRequest.builder()
                .financialInstitutionId(query.getFinancialInstitutionId())
                .amount(query.getAmount())
                .consentReference(query.getConsentReference())
                .creditorAccountReference(query.getCreditorAccountReference())
                .creditorAccountReferenceType(query.getCreditorAccountReferenceType())
                .creditorName(query.getCreditorName())
                .currency(query.getCurrency())
                .endToEndId(query.getEndToEndId())
                .productType(query.getProductType())
                .redirectUri(query.getRedirectUri())
                .remittanceInformation(query.getRemittanceInformation())
                .remittanceInformationType(query.getRemittanceInformationType())
                .creditorAgent(query.getCreditorAgent())
                .creditorAgentType(query.getCreditorAgentType())
                .debtorAccountReference(query.getDebtorAccountReference())
                .debtorAccountReferenceType(query.getDebtorAccountReferenceType())
                .debtorName(query.getDebtorName())
                .customerIpAddress(query.getCustomerIpAddress())
                .locale(query.getLocale())
                .skipIbanityCompletionCallback(query.isSkipIbanityCompletionCallback())
                .allowFinancialInstitutionRedirectUri(query.isAllowFinancialInstitutionRedirectUri())
                .state(query.getState())
                .startDate(query.getStartDate())
                .endDate(query.getEndDate())
                .executionRule(query.getExecutionRule())
                .frequency(query.getFrequency())
                .financialInstitutionCustomerReference(query.getFinancialInstitutionCustomerReference())
                .build();
    }

    public static Function<DataApiModel, com.ibanity.apis.client.products.xs2a.models.PeriodicPaymentInitiationRequest> getResponseMapping() {
        return dataApiModel -> {
            com.ibanity.apis.client.products.xs2a.models.PeriodicPaymentInitiationRequest periodicPaymentInitiationRequest =
                    IbanityModelMapper.toIbanityModel(dataApiModel, com.ibanity.apis.client.products.xs2a.models.PeriodicPaymentInitiationRequest.class);

            if (dataApiModel.getLinks() != null) {
                periodicPaymentInitiationRequest.setLinks(PeriodicPaymentInitiationAuthorizationLinks.builder()
                        .redirect(dataApiModel.getLinks().getRedirect())
                        .build());
            }

            if (dataApiModel.getRelationships() != null
                    && dataApiModel.getRelationships().get("financialInstitution") != null) {
                RelationshipsApiModel financialInstitution = dataApiModel.getRelationships().get("financialInstitution");
                periodicPaymentInitiationRequest.setFinancialInstitutionId(UUID.fromString(financialInstitution.getData().getId()));
                periodicPaymentInitiationRequest.setFinancialInstitutionLink(
                        FinancialInstitutionLinks.builder()
                                .related(financialInstitution.getLinks().getRelated())
                                .build());
            }

            return periodicPaymentInitiationRequest;
        };
    }
}
