package com.ibanity.apis.client.products.xs2a.mappers;

import com.ibanity.apis.client.jsonapi.DataApiModel;
import com.ibanity.apis.client.jsonapi.RelationshipsApiModel;
import com.ibanity.apis.client.mappers.IbanityModelMapper;
import com.ibanity.apis.client.products.xs2a.models.create.BulkPaymentInitiationRequestCreationQuery;
import com.ibanity.apis.client.products.xs2a.models.links.BulkPaymentInitiationAuthorizationLinks;
import com.ibanity.apis.client.products.xs2a.models.links.FinancialInstitutionLinks;
import com.ibanity.apis.client.products.xs2a.services.impl.BulkPaymentInitiationRequestServiceImpl;

import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BulkPaymentInitiationRequestMapper {

    public static BulkPaymentInitiationRequestServiceImpl.BulkPaymentInitiationRequest getRequestMapping(BulkPaymentInitiationRequestCreationQuery query) {
        return BulkPaymentInitiationRequestServiceImpl.BulkPaymentInitiationRequest.builder()
                .financialInstitutionId(query.getFinancialInstitutionId())
                .consentReference(query.getConsentReference())
                .productType(query.getProductType())
                .redirectUri(query.getRedirectUri())
                .debtorAccountReference(query.getDebtorAccountReference())
                .debtorAccountReferenceType(query.getDebtorAccountReferenceType())
                .debtorName(query.getDebtorName())
                .customerIpAddress(query.getCustomerIpAddress())
                .locale(query.getLocale())
                .skipIbanityCompletionCallback(query.isSkipIbanityCompletionCallback())
                .allowFinancialInstitutionRedirectUri(query.isAllowFinancialInstitutionRedirectUri())
                .state(query.getState())
                .requestedExecutionDate(query.getRequestedExecutionDate())
                .payments(query.getPayments().stream().map(payment -> {
                    return BulkPaymentInitiationRequestServiceImpl.BulkPaymentInitiationRequest.Payment.builder()
                            .amount(payment.getAmount())
                            .currency(payment.getCurrency())
                            .creditorAgent(payment.getCreditorAgent())
                    .creditorAgentType(payment.getCreditorAgentType())
                    .creditorName(payment.getCreditorName())
                    .creditorAccountReferenceType(payment.getCreditorAccountReferenceType())
                    .creditorAccountReference(payment.getCreditorAccountReference())
                    .endToEndId(payment.getEndToEndId())
                    .remittanceInformation(payment.getRemittanceInformation())
                    .remittanceInformationType(payment.getRemittanceInformationType())
                    .build();
                }).collect(Collectors.toList()))
                .financialInstitutionCustomerReference(query.getFinancialInstitutionCustomerReference())
                .build();
    }

    public static Function<DataApiModel, com.ibanity.apis.client.products.xs2a.models.BulkPaymentInitiationRequest> getResponseMapping() {
        return dataApiModel -> {
            com.ibanity.apis.client.products.xs2a.models.BulkPaymentInitiationRequest bulkPaymentInitiationRequest =
                    IbanityModelMapper.toIbanityModel(dataApiModel, com.ibanity.apis.client.products.xs2a.models.BulkPaymentInitiationRequest.class);

            if (dataApiModel.getLinks() != null) {
                bulkPaymentInitiationRequest.setLinks(BulkPaymentInitiationAuthorizationLinks.builder()
                        .redirect(dataApiModel.getLinks().getRedirect())
                        .build());
            }

            if (dataApiModel.getRelationships() != null
                    && dataApiModel.getRelationships().get("financialInstitution") != null) {
                RelationshipsApiModel financialInstitution = dataApiModel.getRelationships().get("financialInstitution");
                bulkPaymentInitiationRequest.setFinancialInstitutionId(UUID.fromString(financialInstitution.getData().getId()));
                bulkPaymentInitiationRequest.setFinancialInstitutionLink(
                        FinancialInstitutionLinks.builder()
                                .related(financialInstitution.getLinks().getRelated())
                                .build());
            }

            return bulkPaymentInitiationRequest;
        };
    }
}
