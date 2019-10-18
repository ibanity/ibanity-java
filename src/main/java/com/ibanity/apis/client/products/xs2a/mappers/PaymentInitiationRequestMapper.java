package com.ibanity.apis.client.products.xs2a.mappers;

import com.ibanity.apis.client.jsonapi.DataApiModel;
import com.ibanity.apis.client.jsonapi.RelationshipsApiModel;
import com.ibanity.apis.client.mappers.IbanityModelMapper;
import com.ibanity.apis.client.products.xs2a.models.PaymentInitiationRequest;
import com.ibanity.apis.client.products.xs2a.models.create.PaymentInitiationRequestCreationQuery;
import com.ibanity.apis.client.products.xs2a.models.links.FinancialInstitutionLinks;
import com.ibanity.apis.client.products.xs2a.models.links.PaymentInitiationAuthorizationLinks;

import java.util.function.Function;

public class PaymentInitiationRequestMapper {

    public static PaymentInitiationRequest getRequestMapping(PaymentInitiationRequestCreationQuery query) {
        return PaymentInitiationRequest.builder()
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
                .build();
    }

    public static Function<DataApiModel, PaymentInitiationRequest> getResponseMapping() {
        return dataApiModel -> {
            PaymentInitiationRequest paymentInitiationRequest =
                    IbanityModelMapper.toIbanityModel(dataApiModel, PaymentInitiationRequest.class);

            if (dataApiModel.getLinks() != null) {
                paymentInitiationRequest.setLinks(PaymentInitiationAuthorizationLinks.builder()
                        .redirect(dataApiModel.getLinks().getRedirect())
                        .build());
            }

            if (dataApiModel.getRelationships() != null
                    && dataApiModel.getRelationships().get("financialInstitution") != null) {
                RelationshipsApiModel financialInstitution = dataApiModel.getRelationships().get("financialInstitution");
                paymentInitiationRequest.setFinancialInstitutionId(financialInstitution.getData().getId());
                paymentInitiationRequest.setFinancialInstitutionLink(
                        FinancialInstitutionLinks.builder()
                                .related(financialInstitution.getLinks().getRelated())
                                .build());
            }

            return paymentInitiationRequest;
        };
    }
}