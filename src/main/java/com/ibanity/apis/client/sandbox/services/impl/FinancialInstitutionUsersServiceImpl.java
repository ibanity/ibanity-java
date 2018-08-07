package com.ibanity.apis.client.sandbox.services.impl;

import com.ibanity.apis.client.configuration.IbanityConfiguration;
import com.ibanity.apis.client.paging.IbanityPagingSpec;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionUser;
import com.ibanity.apis.client.sandbox.models.factory.create.FinancialInstitutionUserCreationQuery;
import com.ibanity.apis.client.sandbox.models.factory.delete.FinancialInstitutionUserDeleteQuery;
import com.ibanity.apis.client.sandbox.models.factory.read.FinancialInstitutionUserReadQuery;
import com.ibanity.apis.client.sandbox.models.factory.read.FinancialInstitutionUsersReadQuery;
import com.ibanity.apis.client.sandbox.models.factory.update.FinancialInstitutionUserUpdateQuery;
import com.ibanity.apis.client.sandbox.services.FinancialInstitutionUsersService;
import com.ibanity.apis.client.services.impl.AbstractServiceImpl;
import io.crnk.core.queryspec.QuerySpec;
import io.crnk.core.repository.ResourceRepositoryV2;
import io.crnk.core.resource.list.ResourceList;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class FinancialInstitutionUsersServiceImpl extends AbstractServiceImpl implements FinancialInstitutionUsersService {

    public FinancialInstitutionUsersServiceImpl() {
        super();
    }

    @Override
    public ResourceList<FinancialInstitutionUser> list(final FinancialInstitutionUsersReadQuery usersReadQuery) {
        QuerySpec querySpec = new QuerySpec(FinancialInstitutionUser.class);

        if (usersReadQuery.getPagingSpec() != null) {
            querySpec.setPagingSpec(usersReadQuery.getPagingSpec());
        } else {
            querySpec.setPagingSpec(IbanityPagingSpec.DEFAULT_PAGING_SPEC);
        }

        return getRepository(null)
                .findAll(querySpec);
    }

    @Override
    public FinancialInstitutionUser find(final FinancialInstitutionUserReadQuery userReadQuery) {
        return getRepository(null)
                .findOne(userReadQuery.getFinancialInstitutionUserId(),
                        new QuerySpec(FinancialInstitutionUser.class));
    }

    @Override
    public FinancialInstitutionUser create(final FinancialInstitutionUserCreationQuery userCreationQuery) {
        FinancialInstitutionUser financialInstitutionUser = new FinancialInstitutionUser();
        financialInstitutionUser.setLogin(userCreationQuery.getLogin());
        financialInstitutionUser.setPassword(userCreationQuery.getPassword());
        financialInstitutionUser.setLastName(userCreationQuery.getLastName());
        financialInstitutionUser.setFirstName(userCreationQuery.getFirstName());
        return getRepository(userCreationQuery.getIdempotencyKey())
                .create(financialInstitutionUser);
    }

    @Override
    public FinancialInstitutionUser update(final FinancialInstitutionUserUpdateQuery userUpdateQuery) {
        FinancialInstitutionUser financialInstitutionUser = new FinancialInstitutionUser();
        financialInstitutionUser.setId(userUpdateQuery.getFinancialInstitutionUserId());
        financialInstitutionUser.setLogin(userUpdateQuery.getLogin());
        financialInstitutionUser.setPassword(userUpdateQuery.getPassword());
        financialInstitutionUser.setFirstName(userUpdateQuery.getFirstName());
        financialInstitutionUser.setLastName(userUpdateQuery.getLastName());

        return getRepository(userUpdateQuery.getIdempotencyKey())
                .save(financialInstitutionUser);
    }

    @Override
    public void delete(final FinancialInstitutionUserDeleteQuery userDeleteQuery) {
        getRepository(userDeleteQuery.getIdempotencyKey())
                .delete(userDeleteQuery.getFinancialInstitutionUserId());
    }

    private ResourceRepositoryV2<FinancialInstitutionUser, UUID> getRepository(final UUID idempotencyKey) {
        String finalPath = StringUtils.removeEnd(
                IbanityConfiguration.getApiUrls().getSandbox().getFinancialInstitutionUsers()
                        .replace(FinancialInstitutionUser.RESOURCE_PATH, "")
                        .replace(FinancialInstitutionUser.API_URL_TAG_ID, ""), "//");

        return getApiClient(finalPath, null, idempotencyKey).getRepositoryForType(FinancialInstitutionUser.class);
    }
}
