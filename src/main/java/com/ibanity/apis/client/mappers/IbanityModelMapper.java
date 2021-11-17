package com.ibanity.apis.client.mappers;

import com.ibanity.apis.client.jsonapi.CollectionApiModel;
import com.ibanity.apis.client.jsonapi.DataApiModel;
import com.ibanity.apis.client.jsonapi.ResourceApiModel;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.models.IbanityModel;
import com.ibanity.apis.client.models.webhooks.IbanityWebhooks;
import com.ibanity.apis.client.products.xs2a.mappers.SynchronizationMapper;
import com.ibanity.apis.client.products.xs2a.models.Synchronization;
import com.ibanity.apis.client.utils.IbanityUtils;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ibanity.apis.client.mappers.ModelMapperHelper.getRequestId;
import static com.ibanity.apis.client.mappers.ModelMapperHelper.readResponseContent;
import static java.lang.String.format;
import static java.util.UUID.fromString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class IbanityModelMapper {

    public static <T extends IbanityModel> T mapResource(HttpResponse httpResponse, Class<T> classType) {
        return mapResource(httpResponse, dataApiModel -> toIbanityModel(dataApiModel, classType));
    }

    public static <T extends IbanityModel> T mapResource(HttpResponse httpResponse, Function<DataApiModel, T> customMapping) {
        try {
            String jsonPayload = readResponseContent(httpResponse.getEntity());
            DataApiModel dataApiModel = IbanityUtils.objectMapper().readValue(jsonPayload, ResourceApiModel.class).getData();
            T ibanityModel = customMapping.apply(dataApiModel);
            ibanityModel.setRequestId(getRequestId(httpResponse));
            return ibanityModel;
        } catch (IOException exception) {
            throw new IllegalArgumentException("Response cannot be parsed", exception);
        }
    }

    public static <T extends IbanityModel> IbanityCollection<T> mapCollection(HttpResponse httpResponse, Class<T> classType) {
        return mapCollection(httpResponse, dataApiModel -> toIbanityModel(dataApiModel, classType));
    }

    public static <T extends IbanityModel> IbanityCollection<T> mapCollection(HttpResponse httpResponse, Function<DataApiModel, T> customMapping) {
        try {
            String jsonPayload = readResponseContent(httpResponse.getEntity());
            CollectionApiModel collectionApiModel = IbanityUtils.objectMapper().readValue(jsonPayload, CollectionApiModel.class);
            String requestId = getRequestId(httpResponse);
            return IbanityCollection.<T>builder()
                    .requestId(requestId)
                    .pageLimit(collectionApiModel.getMeta().getPaging().getLimit())
                    .pageNumber(collectionApiModel.getMeta().getPaging().getPageNumber())
                    .pageSize(collectionApiModel.getMeta().getPaging().getPageSize())
                    .totalEntries(collectionApiModel.getMeta().getPaging().getTotalEntries())
                    .totalPages(collectionApiModel.getMeta().getPaging().getTotalPages())
                    .afterCursor(toUUIDNullSafe(collectionApiModel.getMeta().getPaging().getAfter()))
                    .beforeCursor(toUUIDNullSafe(collectionApiModel.getMeta().getPaging().getBefore()))
                    .firstLink(collectionApiModel.getLinks().getFirst())
                    .previousLink(collectionApiModel.getLinks().getPrev())
                    .nextLink(collectionApiModel.getLinks().getNext())
                    .lastLink(collectionApiModel.getLinks().getLast())
                    .latestSynchronization(getLatestSynchronization(collectionApiModel))
                    .items(
                            collectionApiModel.getData().stream()
                                    .map(customMapping)
                                    .peek(value -> { value.setRequestId(requestId); })
                                    .collect(Collectors.toList())
                    )
                    .build();
        } catch (IOException exception) {
            throw new IllegalArgumentException("Response cannot be parsed", exception);
        }
    }

    public static <T extends IbanityWebhooks> T mapWebhookResource(String payload, Function<DataApiModel, T> customMapping) {
        try {
            DataApiModel dataApiModel = IbanityUtils.objectMapper().readValue(payload, ResourceApiModel.class).getData();
            return customMapping.apply(dataApiModel);
        } catch (IOException exception) {
            throw new IllegalArgumentException("Response cannot be parsed", exception);
        }
    }

    private static Synchronization getLatestSynchronization(CollectionApiModel collectionApiModel) {
        if(collectionApiModel.getMeta().getLatestSynchronization() != null) {
            return SynchronizationMapper.map(collectionApiModel.getMeta().getLatestSynchronization());
        } else {
            return null;
        }
    }

    public static <T extends IbanityModel> T toIbanityModel(DataApiModel data, Class<T> classType) {
        try {
            T clientObject = IbanityUtils.objectMapper().convertValue(data.getAttributes(), classType);
            if (clientObject == null) {
                clientObject = classType.newInstance();
            }
            clientObject.setId(fromString(data.getId()));
            if (data.getLinks() != null) {
                clientObject.setSelfLink(data.getLinks().getSelf());
            }

            return clientObject;
        } catch (InstantiationException | IllegalAccessException exception) {
            throw new RuntimeException(format("Instantiation of class %s is impossible for default constructor", classType), exception);
        }
    }

    public static <T extends IbanityWebhooks> T toIbanityWebhooks(DataApiModel data, Class<T> classType) {
        try {
            T clientObject = IbanityUtils.objectMapper().convertValue(data.getAttributes(), classType);
            if (clientObject == null) {
                clientObject = classType.newInstance();
            }

            clientObject.setId(fromString(data.getId()));

            return clientObject;
        } catch (InstantiationException | IllegalAccessException exception) {
            throw new RuntimeException(format("Instantiation of class %s is impossible for default constructor", classType), exception);
        }
    }

    public static UUID toUUIDNullSafe(String value) {
        return isNotBlank(value) ? fromString(value) : null;
    }
}
