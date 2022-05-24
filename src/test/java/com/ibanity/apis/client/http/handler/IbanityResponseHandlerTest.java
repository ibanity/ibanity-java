package com.ibanity.apis.client.http.handler;

import com.ibanity.apis.client.exceptions.IbanityClientException;
import com.ibanity.apis.client.exceptions.IbanityServerException;
import com.ibanity.apis.client.models.ErrorMeta;
import com.ibanity.apis.client.models.FinancialInstitutionResponse;
import com.ibanity.apis.client.models.IbanityError;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IbanityResponseHandlerTest {

    private static final String REQUEST_ID = "thisIsARequestId";
    private static final String IBANITY_REQUEST_ID_HEADER = "ibanity-request-id";
    private IbanityResponseHandler ibanityResponseHandler = new IbanityResponseHandler();

    @Mock
    private HttpResponse httpResponse;

    @Test
    void handleResponse() throws IOException {
        when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(dummyProtocolVersion(), 200, ""));

        HttpResponse actual = ibanityResponseHandler.handleResponse(httpResponse);

        assertThat(actual).isEqualTo(httpResponse);
    }

    @Test
    void handleResponse_whenServerError_thenThrowIbanityServerSideException() {
        //language=JSON
        String expected = errorPayloadWithJson();

        when(httpResponse.getEntity()).thenReturn(EntityBuilder.create().setText(expected).build());
        when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(dummyProtocolVersion(), 500, ""));
        when(httpResponse.getFirstHeader(IBANITY_REQUEST_ID_HEADER)).thenReturn(new BasicHeader(IBANITY_REQUEST_ID_HEADER, REQUEST_ID));

        IbanityServerException actual = assertThrows(IbanityServerException.class, () -> ibanityResponseHandler.handleResponse(httpResponse));

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(new IbanityServerException(createExpectedErrorsWithJson(), 500, REQUEST_ID));
    }

    @Test
    void handleResponse_whenResourceNotFound_thenThrowIbanityClientSideException() {
        //language=JSON
        String expected = errorPayloadWithJson();

        when(httpResponse.getEntity()).thenReturn(EntityBuilder.create().setText(expected).build());
        when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(dummyProtocolVersion(), 404, ""));
        when(httpResponse.getFirstHeader(IBANITY_REQUEST_ID_HEADER)).thenReturn(new BasicHeader(IBANITY_REQUEST_ID_HEADER, REQUEST_ID));

        IbanityClientException actual = assertThrows(IbanityClientException.class, () -> ibanityResponseHandler.handleResponse(httpResponse));

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(new IbanityServerException(createExpectedErrorsWithJson(), 404, REQUEST_ID));
    }

    @Test
    void handleResponse_whenResourceNotFoundAndNoRequestId_thenThrowIbanityClientSideException() {
        //language=JSON
        String expected = errorPayloadWithHtml();

        when(httpResponse.getEntity()).thenReturn(EntityBuilder.create().setText(expected).build());
        when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(dummyProtocolVersion(), 404, ""));
        when(httpResponse.getFirstHeader(IBANITY_REQUEST_ID_HEADER)).thenReturn(null);

        IbanityClientException actual = assertThrows(IbanityClientException.class, () -> ibanityResponseHandler.handleResponse(httpResponse));

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(new IbanityServerException(createExpectedErrorsWithHtml(), 404, null));
    }

    private List<IbanityError> createExpectedErrors(String body) {
        return Collections.singletonList(IbanityError.builder()
                .code("invalidCredentials")
                .detail("Your credentials are invalid.")
                .meta(ErrorMeta.builder()
                        .financialInstitutionResponse(FinancialInstitutionResponse.builder()
                                .body(body)
                                .requestId("354fwfwef4w684")
                                .statusCode(500)
                                .timestamp(Instant.parse("2019-05-09T09:18:00.000Z"))
                                .requestUri("http://google.com")
                                .build())
                        .build())
                .build());
    }

    private List<IbanityError> createExpectedErrorsWithJson() {
        return createExpectedErrors("{\"tppMessages\":[{\"category\":\"ERROR\",\"code\":\"NOT_FOUND\",\"text\":\"3.2 - Not Found\"}]}");
    }

    private List<IbanityError> createExpectedErrorsWithHtml() {
        return createExpectedErrors("<html><head>SomeHtml</head></html>");
    }

    private String errorPayloadWithHtml() {
        return "{\n" +
                "  \"errors\": [\n" +
                "    {\n" +
                "      \"code\": \"invalidCredentials\",\n" +
                "      \"detail\": \"Your credentials are invalid.\",\n" +
                "      \"meta\": {\n" +
                "        \"financialInstitutionResponse\": {\n" +
                "          \"statusCode\": 500,\n" +
                "          \"body\": \"<html><head>SomeHtml</head></html>\",\n" +
                "          \"requestId\": \"354fwfwef4w684\",\n" +
                "          \"timestamp\": \"2019-05-09T09:18:00.000Z\",\n" +
                "          \"requestUri\": \"http://google.com\"\n" +
                "        }\n" +
                "      " +
                "}\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }

    private String errorPayloadWithJson() {
        //language=JSON
        return "{\n" +
                "  \"errors\": [\n" +
                "    {\n" +
                "      \"code\": \"invalidCredentials\",\n" +
                "      \"detail\": \"Your credentials are invalid.\",\n" +
                "      \"meta\": {\n" +
                "        \"financialInstitutionResponse\": {\n" +
                "          \"statusCode\": 500,\n" +
                "          \"body\": {\n" +
                "            \"tppMessages\": [\n" +
                "              {\n" +
                "                \"category\": \"ERROR\",\n" +
                "                \"code\": \"NOT_FOUND\",\n" +
                "                \"text\": \"3.2 - Not Found\"\n" +
                "              }\n" +
                "            ]\n" +
                "          },\n" +
                "          \"requestId\": \"354fwfwef4w684\",\n" +
                "          \"timestamp\": \"2019-05-09T09:18:00.000Z\",\n" +
                "          \"requestUri\": \"http://google.com\"\n" +
                "        }\n" +
                "      " +
                "}\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }

    private ProtocolVersion dummyProtocolVersion() {
        return new ProtocolVersion("", 0, 0);
    }
}
