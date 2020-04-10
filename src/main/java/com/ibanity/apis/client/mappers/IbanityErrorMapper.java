package com.ibanity.apis.client.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ibanity.apis.client.jsonapi.FinancialInstitutionResponseApiModel;
import com.ibanity.apis.client.jsonapi.IbanityErrorApiModel;
import com.ibanity.apis.client.models.ErrorMeta;
import com.ibanity.apis.client.models.FinancialInstitutionResponse;
import com.ibanity.apis.client.models.IbanityError;

import static com.ibanity.apis.client.utils.IbanityUtils.objectMapper;

public class IbanityErrorMapper {

    public static IbanityError map(IbanityErrorApiModel ibanityErrorApiModel) {
        IbanityError.IbanityErrorBuilder ibanityErrorBuilder = IbanityError.builder()
                .code(ibanityErrorApiModel.getCode())
                .detail(ibanityErrorApiModel.getDetail());
        if (ibanityErrorApiModel.getMeta() != null) {
            ErrorMeta.ErrorMetaBuilder errorMetaBuilder = ErrorMeta.builder();
            FinancialInstitutionResponseApiModel financialInstitutionResponseApiModel = ibanityErrorApiModel.getMeta().getFinancialInstitutionResponse();
            if (financialInstitutionResponseApiModel != null) {
                FinancialInstitutionResponse financialInstitutionResponse = FinancialInstitutionResponse.builder()
                        .requestUri(financialInstitutionResponseApiModel.getRequestUri())
                        .statusCode(financialInstitutionResponseApiModel.getStatusCode())
                        .body(parseBody(financialInstitutionResponseApiModel))
                        .requestId(financialInstitutionResponseApiModel.getRequestId())
                        .timestamp(financialInstitutionResponseApiModel.getTimestamp())
                        .build();
                errorMetaBuilder.financialInstitutionResponse(financialInstitutionResponse);
            }

            ibanityErrorBuilder
                    .meta(errorMetaBuilder.build());
        }
        return ibanityErrorBuilder
                .build();
    }

    private static String parseBody(FinancialInstitutionResponseApiModel financialInstitutionResponseApiModel) {
        Object body = financialInstitutionResponseApiModel.getBody();
        if (body == null) {
            return null;
        } else if (body instanceof String) {
            return (String) body;
        } else {
            try {
                return objectMapper().writeValueAsString(body);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Invalid payload", e);
            }
        }
    }

}
