package com.ibanity.apis.client.products.ponto_connect.helpers;

import com.ibanity.apis.client.products.ponto_connect.models.Filter;
import org.apache.http.client.utils.URIBuilder;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilterHelperTest {

    @Test
    public void filterStringContains() throws URISyntaxException {
        Filter actual = Filter.builder()
                .field("myField")
                .contains("myValue")
                .build();
        assertThat(encodeQueryParams(actual)).isEqualTo("filter[myField][contains]=myValue");
    }

    @Test
    public void filterStringEq() throws URISyntaxException {
        Filter actual = Filter.builder()
                .field("myField")
                .eq("myValue")
                .build();
        assertThat(encodeQueryParams(actual)).isEqualTo("filter[myField][eq]=myValue");
    }

    @Test
    public void filterStringIn() throws URISyntaxException {
        Filter actual = Filter.builder()
                .field("myField")
                .in("myValue1,myValue2")
                .build();
        assertThat(encodeQueryParams(actual)).isEqualTo("filter[myField][in]=myValue1,myValue2");
    }

    @Test
    public void filterStringInBoolean() throws URISyntaxException {
        Filter actual = Filter.builder()
                .field("myField")
                .in("true")
                .build();
        assertThat(encodeQueryParams(actual)).isEqualTo("filter[myField][in]=true");
    }

    @Test
    public void filterFieldNull() {
        assertThrows(NullPointerException.class, () -> {
            Filter.builder()
                    .in("true")
                    .build();
        });
    }

    @Test
    public void filterStringAllOperators() throws URISyntaxException {
        Filter actual = Filter.builder()
                .field("myField")
                .in("myValue1,myValue2")
                .contains("myValue")
                .eq("myValue1")
                .build();
        assertThat(encodeQueryParams(actual)).isEqualTo("filter[myField][in]=myValue1,myValue2&filter[myField][eq]=myValue1&filter[myField][contains]=myValue");
    }

    private String encodeQueryParams(Filter actual) throws URISyntaxException {
        return new URIBuilder("www.ibanity.be")
                .addParameters(FilterHelper.encode(actual)).build().getQuery();
    }
}
