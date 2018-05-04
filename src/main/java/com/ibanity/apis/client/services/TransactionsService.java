package com.ibanity.apis.client.services;

import com.ibanity.apis.client.models.Account;
import com.ibanity.apis.client.models.CustomerAccessToken;
import com.ibanity.apis.client.paging.PagingSpec;
import com.ibanity.apis.client.models.Transaction;

import java.util.List;
import java.util.UUID;

/**
 * Service for Transactions related APIs
 */
public interface TransactionsService {
    /**
     * Get account's transactions
     * note: In a transaction object's, the account property object, only the account.id is populated
     * @param customerAccessToken the token generated by the call to the corresponding service (See @link com.ibanity.services.CustomerAccessTokenService#createCustomerAccessToken(CustomerAccessToken)}
     * @param account the account for which the transactions need to be provided. The account.financialInstitution.id has to be set as well as the account.id!
     * @return list of customer's accounts transactions in the specified financial institution (including paginations details)
     */
    List<Transaction> getAccountTransactions(CustomerAccessToken customerAccessToken, Account account);

    /**
     * Get account's transactions
     * note: In a transaction object's, the account property object, only the account.id is populated
     * @param customerAccessToken the token generated by the call to the corresponding service (See @link com.ibanity.services.CustomerAccessTokenService#createCustomerAccessToken(CustomerAccessToken)}
     * @param account the account for which the transactions need to be provided. The account.financialInstitution.id has to be set as well as the account.id!
     * @param pagingSpec The paging specification to be used for tuning the resulting list
     * @return list of customer's accounts transactions in the specified financial institution (including paginations details)
     */
    List<Transaction> getAccountTransactions(CustomerAccessToken customerAccessToken, Account account, PagingSpec pagingSpec);

    /**
     * Get a specific transaction details
     * @param customerAccessToken the token generated by the call to the corresponding service (See @link com.ibanity.services.CustomerAccessTokenService#createCustomerAccessToken(CustomerAccessToken)}
     * @param account the account to which the transaction is linked (note: the account.financialInstitution.id must be set as well as the account.id)
     * @param transactionId the transaction Id for which we want the details for
     * @return the requested transaction details
     */
    Transaction getAccountTransaction(CustomerAccessToken customerAccessToken, Account account, UUID transactionId);
}