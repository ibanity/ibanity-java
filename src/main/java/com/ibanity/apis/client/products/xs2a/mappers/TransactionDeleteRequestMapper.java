package com.ibanity.apis.client.products.xs2a.mappers;

import com.ibanity.apis.client.jsonapi.DataApiModel;
import com.ibanity.apis.client.jsonapi.TransactionDeleteRequestApiModel;
import com.ibanity.apis.client.products.xs2a.models.TransactionDeleteRequest;
import com.ibanity.apis.client.utils.IbanityUtils;

import java.util.UUID;

public class TransactionDeleteRequestMapper {

    public static TransactionDeleteRequest map(DataApiModel dataApiModel) {
        TransactionDeleteRequestApiModel transactionDeleteRequestApiModel = IbanityUtils.objectMapper().convertValue(dataApiModel.getAttributes(), TransactionDeleteRequestApiModel.class);
        TransactionDeleteRequest result = TransactionDeleteRequest.builder()
                .beforeDate(transactionDeleteRequestApiModel.getBeforeDate())
                .id(UUID.fromString(dataApiModel.getId()))
                .build();

        if (dataApiModel.getLinks() != null) {
            result.setSelfLink(dataApiModel.getLinks().getSelf());
        }
        return result;
    }
}
