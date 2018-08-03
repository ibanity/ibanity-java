package com.ibanity.apis.client.sandbox.services;

import com.ibanity.apis.client.sandbox.models.FinancialInstitutionUser;
import com.ibanity.apis.client.sandbox.models.factory.create.FinancialInstitutionUserCreationQuery;
import com.ibanity.apis.client.sandbox.models.factory.delete.FinancialInstitutionUserDeleteQuery;
import com.ibanity.apis.client.sandbox.models.factory.read.FinancialInstitutionUserReadQuery;
import com.ibanity.apis.client.sandbox.models.factory.read.FinancialInstitutionUsersReadQuery;
import com.ibanity.apis.client.sandbox.models.factory.update.FinancialInstitutionUserUpdateQuery;

import java.util.List;

public interface FinancialInstitutionUsersService {

    FinancialInstitutionUser create(FinancialInstitutionUserCreationQuery userCreationQuery);

    List<FinancialInstitutionUser> list(FinancialInstitutionUsersReadQuery usersReadQuery);

    FinancialInstitutionUser find(FinancialInstitutionUserReadQuery userReadQuery);

    FinancialInstitutionUser update(FinancialInstitutionUserUpdateQuery userUpdateQuery);

    void delete(FinancialInstitutionUserDeleteQuery userDeleteQuery);
}
