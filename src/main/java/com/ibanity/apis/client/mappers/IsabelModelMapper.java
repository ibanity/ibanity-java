package com.ibanity.apis.client.mappers;

import com.ibanity.apis.client.jsonapi.isabel_connect.CollectionApiModel;
import com.ibanity.apis.client.jsonapi.isabel_connect.DataApiModel;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.jsonapi.isabel_connect.ResourceApiModel;
import com.ibanity.apis.client.models.IsabelCollection;
import com.ibanity.apis.client.products.isabel_connect.models.IsabelModel;
import com.ibanity.apis.client.utils.IbanityUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ibanity.apis.client.http.handler.IbanityResponseHandler.IBANITY_REQUEST_ID_HEADER;
import static java.lang.String.format;
import static org.apache.http.util.EntityUtils.consumeQuietly;

public class IsabelModelMapper {

    private static final String DEFAULT_ENCODING = "UTF-8";

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

    public static String getRequestId(HttpResponse httpResponse) {
        Header header = httpResponse.getFirstHeader(IBANITY_REQUEST_ID_HEADER);
        return header == null ? null : header.getValue();
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
            clientObject.setId(data.getId());

            return clientObject;
        } catch (InstantiationException | IllegalAccessException exception) {
            throw new RuntimeException(format("Instantiation of class %s is impossible for default constructor", classType), exception);
        }
    }

    public static RequestApiModel buildRequest(String resourceType, Object attributes) {
        return buildRequest(resourceType, attributes, null);
    }

    public static RequestApiModel buildRequest(String resourceType, Object attributes, Object meta) {
        return RequestApiModel.builder()
                .data(
                        RequestApiModel.RequestDataApiModel.builder()
                                .type(resourceType)
                                .meta(meta)
                                .attributes(attributes)
                                .build()
                )
                .build();
    }

    public static String readResponseContent(HttpEntity entity) throws IOException {
        try {
            return IOUtils.toString(entity.getContent(), DEFAULT_ENCODING);
        } finally {
            consumeQuietly(entity);
        }
    }
}

