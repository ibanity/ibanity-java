package com.ibanity.apis.client.configuration;

import io.crnk.core.resource.links.LinksInformation;

public class ApiIUrls implements LinksInformation {

    private Sandbox sandbox;
    private Customer customer;
    private String financialInstitutions;
    private String customerAccessTokens;

    public Sandbox getSandbox() {
        return sandbox;
    }

    public void setSandbox(final Sandbox sandbox) {
        this.sandbox = sandbox;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(final Customer customer) {
        this.customer = customer;
    }

    public String getFinancialInstitutions() {
        return financialInstitutions;
    }

    public void setFinancialInstitutions(final String financialInstitutions) {
        this.financialInstitutions = financialInstitutions;
    }

    public String getCustomerAccessTokens() {
        return customerAccessTokens;
    }

    public void setCustomerAccessTokens(final String customerAccessTokens) {
        this.customerAccessTokens = customerAccessTokens;
    }

    public class Sandbox {
        private String financialInstitutions;
        private String financialInstitutionUsers;
        private FinancialInstitution financialInstitution;

        public String getFinancialInstitutions() {
            return financialInstitutions;
        }

        public void setFinancialInstitutions(final String financialInstitutions) {
            this.financialInstitutions = financialInstitutions;
        }

        public String getFinancialInstitutionUsers() {
            return financialInstitutionUsers;
        }

        public void setFinancialInstitutionUsers(final String financialInstitutionUsers) {
            this.financialInstitutionUsers = financialInstitutionUsers;
        }

        public FinancialInstitution getFinancialInstitution() {
            return financialInstitution;
        }

        public void setFinancialInstitution(final FinancialInstitution financialInstitution) {
            this.financialInstitution = financialInstitution;
        }

        public class FinancialInstitution {
            private String financialInstitutionAccounts;
            private FinancialInstitutionAccount financialInstitutionAccount;

            public String getFinancialInstitutionAccounts() {
                return financialInstitutionAccounts;
            }

            public void setFinancialInstitutionAccounts(final String financialInstitutionAccounts) {
                this.financialInstitutionAccounts = financialInstitutionAccounts;
            }

            public FinancialInstitutionAccount getFinancialInstitutionAccount() {
                return financialInstitutionAccount;
            }

            public void setFinancialInstitutionAccount(final FinancialInstitutionAccount financialInstitutionAccount) {
                this.financialInstitutionAccount = financialInstitutionAccount;
            }

            public class FinancialInstitutionAccount {
                private String financialInstitutionTransactions;

                public String getFinancialInstitutionTransactions() {
                    return financialInstitutionTransactions;
                }

                public void setFinancialInstitutionTransactions(final String financialInstitutionTransactions) {
                    this.financialInstitutionTransactions = financialInstitutionTransactions;
                }
            }
        }
    }

    public class Customer {
        private String financialInstitutions;
        private String accounts;
        private FinancialInstitution financialInstitution;

        public Customer(final String financialInstitutions, final String accounts) {
            this.financialInstitutions = financialInstitutions;
            this.accounts = accounts;
        }

        public Customer() {
        }

        public String getFinancialInstitutions() {
            return financialInstitutions;
        }

        public void setFinancialInstitutions(final String financialInstitutions) {
            this.financialInstitutions = financialInstitutions;
        }

        public String getAccounts() {
            return accounts;
        }

        public void setAccounts(final String accounts) {
            this.accounts = accounts;
        }

        public FinancialInstitution getFinancialInstitution() {
            return financialInstitution;
        }

        public void setFinancialInstitution(final FinancialInstitution financialInstitution) {
            this.financialInstitution = financialInstitution;
        }

        public class FinancialInstitution {
            private String transactions;
            private String paymentInitiationRequests;
            private String accounts;
            private String accountInformationAccessRequests;

            public String getTransactions() {
                return transactions;
            }

            public void setTransactions(final String transactions) {
                this.transactions = transactions;
            }

            public String getPaymentInitiationRequests() {
                return paymentInitiationRequests;
            }

            public void setPaymentInitiationRequests(final String paymentInitiationRequests) {
                this.paymentInitiationRequests = paymentInitiationRequests;
            }

            public String getAccounts() {
                return accounts;
            }

            public void setAccounts(final String accounts) {
                this.accounts = accounts;
            }

            public String getAccountInformationAccessRequests() {
                return accountInformationAccessRequests;
            }

            public void setAccountInformationAccessRequests(final String accountInformationAccessRequests) {
                this.accountInformationAccessRequests = accountInformationAccessRequests;
            }
        }
    }
}
