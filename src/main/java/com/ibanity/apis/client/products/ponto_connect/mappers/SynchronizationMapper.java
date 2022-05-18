package com.ibanity.apis.client.products.ponto_connect.mappers;

import com.ibanity.apis.client.jsonapi.DataApiModel;
import com.ibanity.apis.client.jsonapi.SynchronizationApiModel;
import com.ibanity.apis.client.mappers.IbanityErrorMapper;
import com.ibanity.apis.client.products.ponto_connect.models.Synchronization;
import com.ibanity.apis.client.products.ponto_connect.models.links.UpdatedTransactionsLinks;
import com.ibanity.apis.client.utils.IbanityUtils;

import java.util.UUID;
import java.util.stream.Collectors;

public class SynchronizationMapper {

    public static Synchronization map(DataApiModel dataApiModel) {
        SynchronizationApiModel synchronizationApiModel = IbanityUtils.objectMapper().convertValue(dataApiModel.getAttributes(), SynchronizationApiModel.class);
        Synchronization result = Synchronization.builder()
                .resourceId(synchronizationApiModel.getResourceId())
                .subtype(synchronizationApiModel.getSubtype())
                .createdAt(synchronizationApiModel.getCreatedAt())
                .updatedAt(synchronizationApiModel.getUpdatedAt())
                .resourceType(synchronizationApiModel.getResourceType())
                .status(synchronizationApiModel.getStatus())
                .errors(synchronizationApiModel.getErrors().stream()
                        .map(IbanityErrorMapper::map)
                        .collect(Collectors.toList()))
                .id(UUID.fromString(dataApiModel.getId()))
                .build();

        if (dataApiModel.getLinks() != null) {
            result.setSelfLink(dataApiModel.getLinks().getSelf());
        }

        if (dataApiModel.getRelationships() != null
                && dataApiModel.getRelationships().get("updatedTransactions") != null
                && dataApiModel.getRelationships().get("updatedTransactions").getLinks() != null) {
            result.setUpdatedTransactionsLinks(UpdatedTransactionsLinks.builder()
                    .related(dataApiModel.getRelationships().get("updatedTransactions").getLinks().getRelated())
                    .build());
        }

        return result;
    }
}
