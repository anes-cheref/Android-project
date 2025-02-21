package com.example.tp_mobile.model;

public class Country {
    private Integer flagResId;
    private String name;
    private Integer code;

    public Country(Integer flag, String name, Integer code) {
        this.code = code;
        this.name = name;
        this.flagResId = flag;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Integer getFlagResId() {
        return flagResId;
    }
}
