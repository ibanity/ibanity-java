package com.ibanity.apis.client.mappers;

import com.ibanity.apis.client.jsonapi.isabel_connect.CollectionApiModel;
import com.ibanity.apis.client.jsonapi.isabel_connect.DataApiModel;
import com.ibanity.apis.client.jsonapi.isabel_connect.ResourceApiModel;
import com.ibanity.apis.client.models.IsabelCollection;
import com.ibanity.apis.client.products.isabel_connect.models.IsabelModel;
import com.ibanity.apis.client.utils.IbanityUtils;
import org.apache.http.HttpResponse;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.io.IOException;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.PropertyUtils;

import static com.ibanity.apis.client.mappers.ModelMapperHelper.getRequestId;
import static com.ibanity.apis.client.mappers.ModelMapperHelper.readResponseContent;
import static java.lang.String.format;
import static java.util.UUID.fromString;

public class IsabelModelMapper {

    public static <T extends IsabelModel> T mapResource(HttpResponse httpResponse, Class<T> classType) {
        return mapResource(httpResponse, dataApiModel -> toIsabelModel(dataApiModel, classType));
    }
    public static <T extends IsabelModel> T mapResource(HttpResponse httpResponse, Function<DataApiModel, T> customMapping) {
        try {
            String jsonPayload = readResponseContent(httpResponse.getEntity());
            DataApiModel dataApiModel = IbanityUtils.objectMapper().readValue(jsonPayload, ResourceApiModel.class).getData();
            T isabelModel = customMapping.apply(dataApiModel);
            isabelModel.setRequestId(getRequestId(httpResponse));
            return isabelModel;
        } catch (IOException exception) {
            throw new IllegalArgumentException("Response cannot be parsed", exception);
        }
    }

    public static <T extends IsabelModel> IsabelCollection<T> mapCollection(HttpResponse httpResponse, Class<T> classType) {
        return mapCollection(httpResponse, dataApiModel -> toIsabelModel(dataApiModel, classType));
    }

    public static <T extends IsabelModel> IsabelCollection<T> mapCollection(HttpResponse httpResponse, Function<DataApiModel, T> customMapping) {
        try {
            String jsonPayload = readResponseContent(httpResponse.getEntity());
            CollectionApiModel collectionApiModel = IbanityUtils.objectMapper().readValue(jsonPayload, CollectionApiModel.class);
            String requestId = getRequestId(httpResponse);
            return IsabelCollection.<T>builder()
                    .requestId(requestId)
                    .pagingOffset(collectionApiModel.getPaginationOffset())
                    .pagingTotal(collectionApiModel.getPaginationTotal())
                    .items(
                            collectionApiModel.getData().stream()
                                    .map(customMapping)
                                    .peek(value -> value.setRequestId(requestId))
                                    .collect(Collectors.toList())
                    )
                    .build();
        } catch (IOException exception) {
            throw new IllegalArgumentException("Response cannot be parsed", exception);
        }
    }

    public static <T extends IsabelModel> T toIsabelModel(DataApiModel data, Class<T> classType) {
        try {
            T clientObject = IbanityUtils.objectMapper().convertValue(data.getAttributes(), classType);
            if (clientObject == null) {
                clientObject = classType.newInstance();
            }

            Class<?> idFieldType = null;
            try {
                Field field = classType.getDeclaredField("id");
                idFieldType = field.getType();
            } catch(NoSuchFieldException noSuchFieldException) {
                if (classType.getSuperclass() == null) {
                    throw noSuchFieldException;
                }

                ParameterizedType genericSuperClass = (ParameterizedType) classType.getGenericSuperclass();
                idFieldType = (Class) genericSuperClass.getActualTypeArguments()[0];
            }

            if (idFieldType.isAssignableFrom(UUID.class)) {
                clientObject.setId(fromString(data.getId()));
            } else {
                clientObject.setId(data.getId());
            }

            return clientObject;
        } catch (InstantiationException | IllegalAccessException | NoSuchFieldException exception) {
            throw new RuntimeException(format("Instantiation of class %s is impossible for default constructor", classType), exception);
        }
    }
}

