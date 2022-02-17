package com.ibanity.apis.client.products.xs2a.mappers;

import com.ibanity.apis.client.jsonapi.DataApiModel;
import com.ibanity.apis.client.jsonapi.BatchSynchronizationApIModel;
import com.ibanity.apis.client.products.xs2a.models.BatchSynchronization;
import com.ibanity.apis.client.utils.IbanityUtils;

import java.util.UUID;
import java.util.stream.Collectors;

public class BatchSynchronizationMapper {

    public static BatchSynchronization map(DataApiModel dataApiModel) {
        BatchSynchronizationApIModel batchSynchronizationApiModel = IbanityUtils.objectMapper().convertValue(dataApiModel.getAttributes(), BatchSynchronizationApIModel.class);
        BatchSynchronization result = BatchSynchronization.builder()
                .id(UUID.fromString(dataApiModel.getId()))
                .build();
        return result;
    }
}
