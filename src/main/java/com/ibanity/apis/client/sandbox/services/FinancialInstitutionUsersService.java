package com.ibanity.apis.client.sandbox.services;

import com.ibanity.apis.client.models.IbanityCollection;
import com.ibanity.apis.client.sandbox.models.FinancialInstitutionUser;
import com.ibanity.apis.client.sandbox.models.factory.delete.FinancialInstitutionUserDeleteQuery;
import com.ibanity.apis.client.sandbox.models.factory.read.FinancialInstitutionUserReadQuery;
import com.ibanity.apis.client.sandbox.models.factory.read.FinancialInstitutionUsersReadQuery;
import com.ibanity.apis.client.sandbox.models.factory.update.FinancialInstitutionUserUpdateQuery;

public interface FinancialInstitutionUsersService {

    FinancialInstitutionUser create(FinancialInstitutionUserUpdateQuery userCreationQuery);

    IbanityCollection<FinancialInstitutionUser> list(FinancialInstitutionUsersReadQuery usersReadQuery);

    FinancialInstitutionUser find(FinancialInstitutionUserReadQuery userReadQuery);

    FinancialInstitutionUser update(FinancialInstitutionUserUpdateQuery userUpdateQuery);

    FinancialInstitutionUser delete(FinancialInstitutionUserDeleteQuery userDeleteQuery);
}
