package com.ibanity.apis.client.utils;

import com.ibanity.apis.client.paging.IbanityPagingSpec;
import lombok.NonNull;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;

import static org.apache.commons.lang3.StringUtils.removeEnd;

public class URIHelper {

    public static URI buildUri(@NonNull String url, IbanityPagingSpec pagingSpec) {
        try {
            pagingSpec = pagingSpec == null ? IbanityPagingSpec.DEFAULT_PAGING_SPEC : pagingSpec;
            URIBuilder uriBuilder = new URIBuilder(removeEnd(url, "/"));
            addIfNotNull(uriBuilder, "before", pagingSpec.getBefore());
            addIfNotNull(uriBuilder, "after", pagingSpec.getAfter());
            addIfNotNull(uriBuilder, "limit", pagingSpec.getLimit());
            return uriBuilder.build();
        } catch (URISyntaxException exception) {
            throw new IllegalStateException("URL cannot be build", exception);
        }
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
            uriBuilder.addParameter(paramName, "" + paramValue.toString());
        }
    }
}
