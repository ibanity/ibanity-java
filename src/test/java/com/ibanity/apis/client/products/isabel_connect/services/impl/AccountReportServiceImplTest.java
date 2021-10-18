package com.ibanity.apis.client.products.isabel_connect.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.models.IbanityProduct;
import com.ibanity.apis.client.models.IsabelCollection;
import com.ibanity.apis.client.products.isabel_connect.models.AccountReport;
import com.ibanity.apis.client.products.isabel_connect.models.read.AccountReportReadQuery;
import com.ibanity.apis.client.products.isabel_connect.models.read.AccountReportsReadQuery;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadFile;
import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadHttpResponse;
import static java.util.Collections.emptyMap;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AccountReportServiceImplTest {
    private static final String ACCESS_TOKEN = "thisIsAnAccessToken";
    private static final String ACCOUNT_REPORT_ID = "1234001";
    private static final String ACCOUNT_REPORT_ENDPOINT = "https://api.ibanity.localhost/isabel-connect/account-reports/{accountReportId}";
    private static final String LIST_ACCOUNT_REPORTS_ENDPOINT = "https://api.ibanity.localhost/isabel-connect/account-reports?size=10";
    private static final String GET_ACCOUNT_REPORTS_ENDPOINT = "https://api.ibanity.localhost/isabel-connect/account-reports/123456";

    @InjectMocks
    private AccountReportServiceImpl accountReportService;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @BeforeEach
    void setup() {
        when(apiUrlProvider.find(IbanityProduct.IsabelConnect, "accountReports"))
                .thenReturn(ACCOUNT_REPORT_ENDPOINT);
    }

    @Test
    public void list() throws Exception {
        when(ibanityHttpClient.get(new URI(LIST_ACCOUNT_REPORTS_ENDPOINT), emptyMap(), ACCESS_TOKEN))
                .thenReturn(loadHttpResponse("json/isabel-connect/account_reports.json"));

        IsabelCollection<AccountReport> actual = accountReportService.list(AccountReportsReadQuery.builder()
                .accessToken(ACCESS_TOKEN)
                .build());

        Assertions.assertThat(actual.getItems()).containsExactly(createExpected());
        Assertions.assertThat(actual.getPagingOffset()).isEqualTo(0);
        Assertions.assertThat(actual.getPagingTotal()).isEqualTo(2);
    }

    @Test
    public void findWithCustomMapping() throws Exception {
        when(ibanityHttpClient.get(new URI(GET_ACCOUNT_REPORTS_ENDPOINT), emptyMap(), ACCESS_TOKEN))
                .thenReturn(loadHttpResponse("coda/coda-sample.txt"));


        AccountReportReadQuery query = AccountReportReadQuery.builder()
                .accessToken(ACCESS_TOKEN)
                .accountReportId("123456")
                .build();

        List<String> actual = accountReportService.find(query, httpResponse -> {
            List<String> res = null;
            try {
                InputStream content = httpResponse.getEntity().getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content, StandardCharsets.UTF_8));
                res = reader.lines().collect(Collectors.toList());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return res;
        });

        Assertions.assertThat(actual).hasSize(24);
    }

    @Test
    public void find() throws Exception {
        when(ibanityHttpClient.get(new URI(GET_ACCOUNT_REPORTS_ENDPOINT), emptyMap(), ACCESS_TOKEN))
                .thenReturn(loadHttpResponse("coda/coda-sample.txt"));


        AccountReportReadQuery query = AccountReportReadQuery.builder()
                .accessToken(ACCESS_TOKEN)
                .accountReportId("123456")
                .build();

        String actual = accountReportService.find(query);

        String expected = loadFile("coda/coda-sample.txt");
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    private AccountReport createExpected() {
        String[] refs = {"BE96153112434405"};

        return AccountReport.builder()
                .id(ACCOUNT_REPORT_ID)
                .accountReferences(Arrays.asList(refs))
                .fileFormat("CODA")
                .fileName("CODA_20181009_BE96153112434405")
                .fileSize(BigInteger.valueOf(29680L))
                .financialInstitutionName("GringotBank")
                .receivedAt(LocalDateTime.parse("2018-10-09T03:55:00.710"))
                .build();
    }
}
