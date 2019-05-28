package com.ibanity.apis.client.products.xs2a.mappers;

import com.ibanity.apis.client.jsonapi.DataApiModel;
import com.ibanity.apis.client.jsonapi.RelationshipsApiModel;
import com.ibanity.apis.client.mappers.IbanityModelMapper;
import com.ibanity.apis.client.products.xs2a.models.PaymentInitiationRequest;
import com.ibanity.apis.client.products.xs2a.models.factory.create.PaymentInitiationRequestCreationQuery;
import com.ibanity.apis.client.products.xs2a.models.links.FinancialInstitutionLinks;
import com.ibanity.apis.client.products.xs2a.models.links.PaymentAccessLinks;

import java.util.function.Function;

public class PaymentInitiationRequestMapper {

    public static PaymentInitiationRequest getRequestMapping(PaymentInitiationRequestCreationQuery query) {
        PaymentInitiationRequest paymentInitiationRequest = new PaymentInitiationRequest();

        paymentInitiationRequest.setFinancialInstitutionId(query.getFinancialInstitutionId());
        paymentInitiationRequest.setAmount(query.getAmount());
        paymentInitiationRequest.setConsentReference(query.getConsentReference());
        paymentInitiationRequest.setCreditorAccountReference(query.getCreditorAccountReference());
        paymentInitiationRequest.setCreditorAccountReferenceType(query.getCreditorAccountReferenceType());
        paymentInitiationRequest.setCreditorName(query.getCreditorName());
        paymentInitiationRequest.setCurrency(query.getCurrency());
        paymentInitiationRequest.setEndToEndId(query.getEndToEndId());
        paymentInitiationRequest.setProductType(query.getProductType());
        paymentInitiationRequest.setRedirectUri(query.getRedirectUri());
        paymentInitiationRequest.setRemittanceInformation(query.getRemittanceInformation());
        paymentInitiationRequest.setRemittanceInformationType(query.getRemittanceInformationType());
        paymentInitiationRequest.setCreditorAgent(query.getCreditorAgent());
        paymentInitiationRequest.setCreditorAgentType(query.getCreditorAgentType());
        paymentInitiationRequest.setDebtorAccountReference(query.getDebtorAccountReference());
        paymentInitiationRequest.setDebtorAccountReferenceType(query.getDebtorAccountReferenceType());
        paymentInitiationRequest.setDebtorName(query.getDebtorName());
        return paymentInitiationRequest;
    }

    public static Function<DataApiModel, PaymentInitiationRequest> getResponseMapping() {
        return dataApiModel -> {
            PaymentInitiationRequest paymentInitiationRequest =
                    IbanityModelMapper.toIbanityModel(dataApiModel, PaymentInitiationRequest.class);

            if (dataApiModel.getLinks() != null) {
                paymentInitiationRequest.setLinks(PaymentAccessLinks.builder()
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