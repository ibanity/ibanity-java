package com.ibanity.apis.client.products.xs2a.mappers;

import com.ibanity.apis.client.jsonapi.BatchSynchronizationApiModel;
import com.ibanity.apis.client.jsonapi.DataApiModel;
import com.ibanity.apis.client.mappers.IbanityErrorMapper;
import com.ibanity.apis.client.products.xs2a.models.BatchSynchronization;
import com.ibanity.apis.client.utils.IbanityUtils;

import java.util.UUID;
import java.util.stream.Collectors;

public class BatchSynchronizationMapper {

    public static BatchSynchronization map(DataApiModel dataApiModel) {
        BatchSynchronizationApiModel batchSynchronizationApiModel = IbanityUtils.objectMapper().convertValue(dataApiModel.getAttributes(), BatchSynchronizationApiModel.class);
        BatchSynchronization result = BatchSynchronization.builder()
                .subtypes(batchSynchronizationApiModel.getSubtypes())
                .cancelAfter(batchSynchronizationApiModel.getCancelAfter())
                .unlessSynchronizedAfter(batchSynchronizationApiModel.getUnlessSynchronizedAfter())
                .resourceType(batchSynchronizationApiModel.getResourceType())
                .errors(batchSynchronizationApiModel.getErrors().stream()
                        .map(IbanityErrorMapper::map)
                        .collect(Collectors.toList()))
                .id(UUID.fromString(dataApiModel.getId()))
                .build();

        if (dataApiModel.getLinks() != null) {
            result.setSelfLink(dataApiModel.getLinks().getSelf());
        }
        return result;
    }
}
