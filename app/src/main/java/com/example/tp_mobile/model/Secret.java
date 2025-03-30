package com.example.tp_mobile.model;

public abstract class Secret {
    private final String alias;
    private final String data;
    private final String iv;

    public Secret(String alias, String data, String iv) {
        this.alias = alias;
        this.data = data;
        this.iv = iv;
    }

    public String getAlias() {
        return alias;
    }

    public String getData() {
        return data;
    }

    public String getIv() {
        return iv;
    }
}

