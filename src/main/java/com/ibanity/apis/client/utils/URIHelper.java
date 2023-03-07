package com.ibanity.apis.client.utils;

import com.ibanity.apis.client.paging.IbanityOffsetPagingSpec;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.products.isabel_connect.models.read.IsabelPagingSpec;
import com.ibanity.apis.client.products.ponto_connect.helpers.FilterHelper;
import com.ibanity.apis.client.products.ponto_connect.models.Filter;
import lombok.NonNull;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static org.apache.commons.lang3.StringUtils.removeEnd;

public class URIHelper {

    public static URI buildUri(@NonNull String url, IsabelPagingSpec pagingSpec) {
        return buildUri(url, pagingSpec, emptyMap());
    }

    public static URI buildUri(@NonNull String url, IsabelPagingSpec pagingSpec, Map<String, String> queryParameters) {
        try {
            pagingSpec = pagingSpec == null ? IsabelPagingSpec.DEFAULT_PAGING_SPEC : pagingSpec;
            URIBuilder uriBuilder = new URIBuilder(removeEnd(url, "/"));
            addIfNotNull(uriBuilder, "offset", pagingSpec.getOffset());
            addIfNotNull(uriBuilder, "size", pagingSpec.getSize());
            addIfNotNull(uriBuilder, "from", pagingSpec.getFrom());
            addIfNotNull(uriBuilder, "to", pagingSpec.getTo());
            addQueryParameters(uriBuilder, queryParameters);
            return uriBuilder.build();
        } catch (URISyntaxException exception) {
            throw new IllegalStateException("URL cannot be build", exception);
        }
    }

    public static URI buildUri(@NonNull String url, IbanityPagingSpec pagingSpec) {
        return buildUri(url, pagingSpec, emptyList());
    }

    public static URI buildUri(@NonNull String url, IbanityPagingSpec pagingSpec, List<Filter> filters) {
        try {
            pagingSpec = pagingSpec == null ? IbanityPagingSpec.DEFAULT_PAGING_SPEC : pagingSpec;
            URIBuilder uriBuilder = new URIBuilder(removeEnd(url, "/"));
            addIfNotNull(uriBuilder, "page[before]", pagingSpec.getBefore());
            addIfNotNull(uriBuilder, "page[after]", pagingSpec.getAfter());
            addIfNotNull(uriBuilder, "page[limit]", pagingSpec.getLimit());
            addFilters(uriBuilder, filters);
            return uriBuilder.build();
        } catch (URISyntaxException exception) {
            throw new IllegalStateException("URL cannot be build", exception);
        }
    }

    public static URI buildUri(@NonNull String url, IbanityOffsetPagingSpec pagingSpec, List<Filter> filters) {
        try {
            pagingSpec = pagingSpec == null ? IbanityOffsetPagingSpec.DEFAULT_OFFSET_PAGING_SPEC : pagingSpec;
            URIBuilder uriBuilder = new URIBuilder(removeEnd(url, "/"));
            addIfNotNull(uriBuilder, "page[size]", pagingSpec.getPageSize());
            addIfNotNull(uriBuilder, "page[number]", pagingSpec.getPageNumber());
            addFilters(uriBuilder, filters);
            return uriBuilder.build();
        } catch (URISyntaxException exception) {
            throw new IllegalStateException("URL cannot be build", exception);
        }
    }

    private static void addFilters(URIBuilder uriBuilder, List<Filter> filters) {
        filters.forEach(filter -> uriBuilder.addParameters(FilterHelper.encode(filter)));
    }

    public static URI buildUri(@NonNull String url) {
        try {
            return new URIBuilder(url).build();
        } catch (URISyntaxException exception) {
            throw new IllegalStateException("URL cannot be build", exception);
        }
    }

    private static void addIfNotNull(URIBuilder uriBuilder, String paramName, Object paramValue) {
        if (paramValue != null) {
            uriBuilder.addParameter(paramName, paramValue.toString());
        }
    }

    private static void addIfNotNull(URIBuilder uriBuilder, String paramName, LocalDate paramValue) {
        if (paramValue != null) {
            uriBuilder.addParameter(paramName, paramValue.format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
    }

    private static void addQueryParameters(URIBuilder uriBuilder, Map<String, String> parameters) {
        parameters.forEach((key, value) -> addIfNotNull(uriBuilder, key, value));
    }
}
