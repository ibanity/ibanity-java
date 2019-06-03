package com.ibanity.apis.client.http.handler;

import com.ibanity.apis.client.exceptions.IbanityClientSideException;
import com.ibanity.apis.client.exceptions.IbanityServerSideException;
import com.ibanity.apis.client.models.IbanityError;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.message.BasicStatusLine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IbanityResponseHandlerTest {

    private IbanityResponseHandler ibanityResponseHandler = new IbanityResponseHandler();

    @Mock
    private HttpResponse httpResponse;

    @Test
    void handleResponse() throws IOException {
        //language=JSON
        String expected = validPayload();

        when(httpResponse.getEntity()).thenReturn(EntityBuilder.create().setText(expected).build());
        when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(dummyProtocolVersion(), 200, ""));

        String actual = ibanityResponseHandler.handleResponse(httpResponse);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void handleResponse_whenServerError_thenThrowIbanityServerSideException() throws IOException {
        //language=JSON
        String expected = errorPayload();

        when(httpResponse.getEntity()).thenReturn(EntityBuilder.create().setText(expected).build());
        when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(dummyProtocolVersion(), 500, ""));

        IbanityServerSideException actual = assertThrows(IbanityServerSideException.class, () -> {
            ibanityResponseHandler.handleResponse(httpResponse);
        });

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(new IbanityServerSideException(createExpectedErrors()));
    }

    @Test
    void handleResponse_whenResourceNotFound_thenThrowIbanityClientSideException() throws IOException {
        //language=JSON
        String expected = errorPayload();

        when(httpResponse.getEntity()).thenReturn(EntityBuilder.create().setText(expected).build());
        when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(dummyProtocolVersion(), 404, ""));

        IbanityClientSideException actual = assertThrows(IbanityClientSideException.class, () -> {
            ibanityResponseHandler.handleResponse(httpResponse);
        });

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(new IbanityServerSideException(createExpectedErrors()));
    }

    private List<IbanityError> createExpectedErrors() {
        return newArrayList(IbanityError.builder()
                .code("invalidCredentials")
                .detail("Your credentials are invalid.")
                .build());
    }

    private String validPayload() {
        return "{\"message\": \"hello world\"}";
    }

    private String errorPayload() {
        return "{\n" +
                "  \"errors\": [\n" +
                "    {\n" +
                "      \"code\": \"invalidCredentials\",\n" +
                "      \"detail\": \"Your credentials are invalid.\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }

    private ProtocolVersion dummyProtocolVersion() {
        return new ProtocolVersion("", 0, 0);
    }
}