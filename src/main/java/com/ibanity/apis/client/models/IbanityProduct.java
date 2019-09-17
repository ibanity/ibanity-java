package com.ibanity.apis.client.models;

public enum IbanityProduct {

    PontoConnect("ponto-connect"), Xs2a("xs2a");

    private String path;

    IbanityProduct(String path) {
        this.path = path;
    }

    public String path() {
        return path;
    }
}
