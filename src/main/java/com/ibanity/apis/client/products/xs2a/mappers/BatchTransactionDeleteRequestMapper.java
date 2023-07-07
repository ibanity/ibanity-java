package com.ibanity.apis.client.products.xs2a.mappers;

import com.ibanity.apis.client.jsonapi.BatchTransactionDeleteRequestApiModel;
import com.ibanity.apis.client.jsonapi.DataApiModel;
import com.ibanity.apis.client.products.xs2a.models.BatchTransactionDeleteRequest;
import com.ibanity.apis.client.utils.IbanityUtils;

import java.util.UUID;

public class BatchTransactionDeleteRequestMapper {

    public static BatchTransactionDeleteRequest map(DataApiModel dataApiModel) {
        BatchTransactionDeleteRequestApiModel batchTransactionDeleteRequestApiModel = IbanityUtils.objectMapper().convertValue(dataApiModel.getAttributes(), BatchTransactionDeleteRequestApiModel.class);
        BatchTransactionDeleteRequest result = BatchTransactionDeleteRequest.builder()
                .beforeDate(batchTransactionDeleteRequestApiModel.getBeforeDate())
                .id(UUID.fromString(dataApiModel.getId()))
                .build();

        if (dataApiModel.getLinks() != null) {
            result.setSelfLink(dataApiModel.getLinks().getSelf());
        }
        return result;
    }
}
