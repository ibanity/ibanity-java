package com.ibanity.apis.client.products.xs2a.sandbox.services.impl;

import com.ibanity.apis.client.http.IbanityHttpClient;
import com.ibanity.apis.client.jsonapi.RequestApiModel;
import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.products.xs2a.sandbox.models.FinancialInstitutionUser;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.delete.FinancialInstitutionUserDeleteQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.read.FinancialInstitutionUserReadQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.read.FinancialInstitutionUsersReadQuery;
import com.ibanity.apis.client.products.xs2a.sandbox.models.factory.update.FinancialInstitutionUserUpdateQuery;
import com.ibanity.apis.client.services.ApiUrlProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.UUID;

import static com.ibanity.apis.client.helpers.IbanityTestHelper.loadHttpResponse;
import static com.ibanity.apis.client.models.IbanityProduct.Xs2a;
import static java.time.Instant.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FinancialInstitutionUsersServiceImplTest {

    private static final UUID USER_ID = UUID.fromString("868af9c1-8fa8-485a-b798-dfb1f5ae1142");
    private static final UUID PREVIOUS_USER_ID = UUID.fromString("868af9c1-8fa8-485a-b798-dfb1f5ae1141");
    private static final UUID FOLLOWING_USER_ID = UUID.fromString("868af9c1-8fa8-485a-b798-dfb1f5ae1143");

    private static final String USERS_ENDPOINT = "https://api.ibanity.com/sandbox/financial-institution-users";
    private static final String API_SCHEMA_ENDPOINT = "https://api.ibanity.com/sandbox/financial-institution-users/{financialInstitutionUserId}";
    private static final String USER_ID_ENDPOINT = "https://api.ibanity.com/sandbox/financial-institution-users/868af9c1-8fa8-485a-b798-dfb1f5ae1142";
    private static final String NEXT_LINK = "https://api.ibanity.com/sandbox/financial-institution-users?after=d500ddac-27e8-447b-b0a1-dbca8c181b99";
    private static final String PREVIOUS_LINK = "https://api.ibanity.com/sandbox/financial-institution-users?before=9089414a-77f8-46b2-b979-edcfad5c2218";

    @InjectMocks
    private FinancialInstitutionUsersServiceImpl financialInstitutionUsersService;

    @Mock
    private ApiUrlProvider apiUrlProvider;

    @Mock
    private IbanityHttpClient ibanityHttpClient;

    @BeforeEach
    void setUp() {
        when(apiUrlProvider.find(Xs2a, "sandbox", "financialInstitutionUsers"))
                .thenReturn(API_SCHEMA_ENDPOINT);
    }

    @Test
    void list() throws Exception {
        FinancialInstitutionUsersReadQuery query = FinancialInstitutionUsersReadQuery.builder()
                .pagingSpec(IbanityPagingSpec.builder()
                        .after(PREVIOUS_USER_ID)
                        .build())
                .build();

        when(ibanityHttpClient.get(new URI(USERS_ENDPOINT + "?after=868af9c1-8fa8-485a-b798-dfb1f5ae1141&limit=10"))).thenReturn(loadHttpResponse("json/sandbox/list_users.json"));

        IbanityCollection<FinancialInstitutionUser> actual = financialInstitutionUsersService.list(query);

        assertThat(actual.getItems()).containsExactly(createExpected());
        assertThat(actual.getBeforeCursor()).isEqualTo(PREVIOUS_USER_ID);
        assertThat(actual.getAfterCursor()).isEqualTo(FOLLOWING_USER_ID);
        assertThat(actual.getNextLink()).isEqualTo(NEXT_LINK);
        assertThat(actual.getPreviousLink()).isEqualTo(PREVIOUS_LINK);
    }

    @Test
    void find() throws Exception {
        when(ibanityHttpClient.get(new URI(USER_ID_ENDPOINT))).thenReturn(loadHttpResponse("json/sandbox/user.json"));

        FinancialInstitutionUserReadQuery query = FinancialInstitutionUserReadQuery.builder()
                .financialInstitutionUserId(USER_ID)
                .build();

        FinancialInstitutionUser actual = financialInstitutionUsersService.find(query);
        assertThat(actual).isEqualToComparingFieldByFieldRecursively(createExpected());
    }

    @Test
    void delete() throws Exception {
        when(ibanityHttpClient.delete(new URI(USER_ID_ENDPOINT))).thenReturn(loadHttpResponse("json/sandbox/delete_user.json"));

        FinancialInstitutionUserDeleteQuery query = FinancialInstitutionUserDeleteQuery.builder()
                .financialInstitutionUserId(USER_ID)
                .build();

        FinancialInstitutionUser actual = financialInstitutionUsersService.delete(query);

        FinancialInstitutionUser expected = FinancialInstitutionUser.builder().id(USER_ID).build();
        assertThat(actual).isEqualToComparingFieldByFieldRecursively(expected);
    }

    @Test
    void create() throws Exception {
        when(ibanityHttpClient.post(new URI(USERS_ENDPOINT), createRequest())).thenReturn(loadHttpResponse("json/sandbox/user.json"));

        FinancialInstitutionUserUpdateQuery query = FinancialInstitutionUserUpdateQuery.builder()
                .firstName("aFirstName")
                .lastName("aLastName")
                .password("aPassword")
                .login("aLogin")
                .build();
        FinancialInstitutionUser actual = financialInstitutionUsersService.create(query);
        assertThat(actual).isEqualToComparingFieldByFieldRecursively(createExpected());
    }

    @Test
    void update() throws Exception {
        when(ibanityHttpClient.patch(new URI(USER_ID_ENDPOINT), createRequest())).thenReturn(loadHttpResponse("json/sandbox/user.json"));

        FinancialInstitutionUserUpdateQuery query = FinancialInstitutionUserUpdateQuery.builder()
                .firstName("aFirstName")
                .lastName("aLastName")
                .password("aPassword")
                .login("aLogin")
                .financialInstitutionUserId(USER_ID)
                .build();
        FinancialInstitutionUser actual = financialInstitutionUsersService.update(query);
        assertThat(actual).isEqualToComparingFieldByFieldRecursively(createExpected());
    }

    private RequestApiModel createRequest() {
        return RequestApiModel.builder()
                .data(RequestApiModel.RequestDataApiModel.builder()
                        .type(FinancialInstitutionUser.RESOURCE_TYPE)
                        .attributes(FinancialInstitutionUser.builder()
                                .firstName("aFirstName")
                                .lastName("aLastName")
                                .password("aPassword")
                                .login("aLogin")
                                .build())
                        .build())
                .build();
    }

    private FinancialInstitutionUser createExpected() {
        return FinancialInstitutionUser.builder()
                .createdAt(parse("2019-05-09T09:20:00.140764Z"))
                .firstName("Elmer")
                .lastName("Lynch")
                .login("elynch")
                .password("password")
                .updatedAt(parse("2019-05-09T09:20:00.140764Z"))
                .id(USER_ID)
                .selfLink(USER_ID_ENDPOINT)
                .build();
    }
}
