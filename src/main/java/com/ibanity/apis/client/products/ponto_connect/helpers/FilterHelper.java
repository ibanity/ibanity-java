package com.ibanity.apis.client.products.ponto_connect.helpers;

import com.ibanity.apis.client.products.ponto_connect.models.Filter;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class FilterHelper {

    private static final String IN_PATTERN = "filter[%s][in]";
    private static final String EQUALS_PATTERN = "filter[%s][eq]";
    private static final String CONTAINS_PATTERN = "filter[%s][contains]";

    public static List<NameValuePair> encode(Filter filter) {
        List<NameValuePair> filters = new ArrayList<>();

        addPattern(IN_PATTERN, filter.getField(), filter.getIn(), filters);
        addPattern(EQUALS_PATTERN, filter.getField(), filter.getEq(), filters);
        addPattern(CONTAINS_PATTERN, filter.getField(), filter.getContains(), filters);

        return filters;
    }

    private static void addPattern(String pattern, String field, String value, List<NameValuePair> filters) {
        if (isNotBlank(value)) {
            filters.add(new BasicNameValuePair(format(pattern, field), value));
        }
    }
}
