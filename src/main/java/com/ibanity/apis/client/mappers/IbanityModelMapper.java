package com.ibanity.apis.client.mappers;

import com.ibanity.apis.client.jsonapi.CollectionApiModel;
import com.ibanity.apis.client.jsonapi.DataApiModel;
import com.ibanity.apis.client.jsonapi.ResourceApiModel;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.models.IbanityModel;
import com.ibanity.apis.client.network.http.client.IbanityHttpUtils;

import java.io.IOException;
import java.util.stream.Collectors;

public class IbanityModelMapper {

    public static <T extends IbanityModel> IbanityCollection<T> mapCollection(String jsonPayload, Class<T> classType) {
        try {
            CollectionApiModel collectionApiModel = IbanityHttpUtils.objectMapper().readValue(jsonPayload, CollectionApiModel.class);
            return IbanityCollection.<T>builder()
                    .items(
                            collectionApiModel.getData().stream()
                                    .map(dataApiModel -> toModel(dataApiModel, classType))
                                    .collect(Collectors.toList())
                    )
                    .build();
        } catch (IOException e) {
            throw new IllegalArgumentException("Response cannot be parsed", e);
        }
    }

    public static <T extends IbanityModel> T mapResource(String jsonPayload, Class<T> classType) {
        try {
            DataApiModel data = IbanityHttpUtils.objectMapper().readValue(jsonPayload, ResourceApiModel.class).getData();
            return toModel(data, classType);
        } catch (IOException e) {
            throw new IllegalArgumentException("Response cannot be parsed", e);
        }
    }

    private static <T extends IbanityModel> T toModel(DataApiModel data, Class<T> classType) {
        T clientObject = IbanityHttpUtils.objectMapper().convertValue(data.getAttributes(), classType);
        clientObject.setId(data.getId());
        if (data.getLinks() != null) {
            clientObject.setSelfLink(data.getLinks().getSelf());
        }

        return clientObject;
    }
}
