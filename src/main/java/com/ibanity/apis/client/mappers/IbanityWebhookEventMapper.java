package com.ibanity.apis.client.mappers;

import com.ibanity.apis.client.jsonapi.DataApiModel;
import com.ibanity.apis.client.jsonapi.ResourceApiModel;
import com.ibanity.apis.client.models.IbanityWebhookEvent;
import com.ibanity.apis.client.utils.IbanityUtils;

import java.io.IOException;
import java.util.function.Function;

import static java.lang.String.format;
import static java.util.UUID.fromString;

public class IbanityWebhookEventMapper {

    public static <T extends IbanityWebhookEvent> T mapWebhookResource(String payload, Function<DataApiModel, T> customMapping) {
        try {
            DataApiModel dataApiModel = IbanityUtils.objectMapper().readValue(payload, ResourceApiModel.class).getData();
            return customMapping.apply(dataApiModel);
        } catch (IOException exception) {
            throw new IllegalArgumentException("Response cannot be parsed", exception);
        }
    }

    public static <T extends IbanityWebhookEvent> T toIbanityWebhooks(DataApiModel data, Class<T> classType) {
        try {
            T clientObject = IbanityUtils.objectMapper().convertValue(data.getAttributes(), classType);
            if (clientObject == null) {
                clientObject = classType.newInstance();
            }

            clientObject.setId(fromString(data.getId()));
            clientObject.setType(data.getType());

            return clientObject;
        } catch (InstantiationException | IllegalAccessException exception) {
            throw new RuntimeException(format("Instantiation of class %s is impossible for default constructor", classType), exception);
        }
    }
}
