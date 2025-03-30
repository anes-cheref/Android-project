package com.example.tp_mobile.model;

import com.squareup.moshi.Json;

public class ResultLogin {
    @Json(name="error")
    private Boolean error;

    @Json(name="msg")
    private String msg;

    @Json(name="id")
    private String id;

    @Json(name="first")
    private String first;

    @Json(name="last")
    private String last;

    @Json(name="email")
    private String email;

    @Json(name="habitat_id")
    private Integer habitatId;


    public String getMsg() {
        return msg;
    }

    public Boolean getError() {
        return error;
    }

    public String getId() {
        return id;
    }

    public String getFirst() {
        return first;
    }

    public String getLast() {
        return last;
    }

    public String getEmail() {
        return email;
    }

    public Integer getHabitatId() {
        return habitatId;
    }
}
