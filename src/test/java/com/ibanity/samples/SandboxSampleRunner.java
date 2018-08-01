package com.ibanity.samples;

public class SandboxSampleRunner {

    public static void main(String[] args) {
        ClientSandboxSample client  = new ClientSandboxSample();

        client.financialInstitutionRequetSamples();

        client.financialInstitutionUserRequestSamples();

        client.financialInstitutionAccountRequestSamples();

        client.financialInstitutionTransactionRequestSamples();
    }

}
