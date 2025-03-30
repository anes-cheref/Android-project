package com.example.tp_mobile.model;

import com.squareup.moshi.Json;

public class Token {
    @Json(name="token")
    private String token;

    @Json(name="expired_at")
    private String expiration;

    public String getToken() {
        return token;
    }

    public String getExpiration() {
        return expiration;
    }
}
