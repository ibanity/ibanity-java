package com.ibanity.apis.client.products.xs2a.mappers;

import com.ibanity.apis.client.jsonapi.DataApiModel;
import com.ibanity.apis.client.jsonapi.SynchronizationApIModel;
import com.ibanity.apis.client.mappers.IbanityErrorMapper;
import com.ibanity.apis.client.products.xs2a.models.Synchronization;
import com.ibanity.apis.client.utils.IbanityUtils;

import java.util.UUID;
import java.util.stream.Collectors;

public class SynchronizationMapper {

    public static Synchronization map(DataApiModel dataApiModel) {
        SynchronizationApIModel synchronizationApiModel = IbanityUtils.objectMapper().convertValue(dataApiModel.getAttributes(), SynchronizationApIModel.class);
        Synchronization result = Synchronization.builder()
                .resourceId(synchronizationApiModel.getResourceId())
                .subtype(synchronizationApiModel.getSubtype())
                .createdAt(synchronizationApiModel.getCreatedAt())
                .updatedAt(synchronizationApiModel.getUpdatedAt())
                .resourceType(synchronizationApiModel.getResourceType())
                .customerOnline(synchronizationApiModel.isCustomerOnline())
                .customerIpAddress(synchronizationApiModel.getCustomerIpAddress())
                .status(synchronizationApiModel.getStatus())
                .errors(synchronizationApiModel.getErrors().stream()
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
